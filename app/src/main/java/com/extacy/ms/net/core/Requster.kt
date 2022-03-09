package com.extacy.ms.net.core

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.extacy.ms.common.utils.withDefers
import com.extacy.ms.extensions.toJson
import com.extacy.ms.net.view.LoadingProgress
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import okhttp3.HttpUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception
import java.lang.reflect.Type
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList

typealias CompleteHandler<Res> = (Res) -> Unit
typealias FailedHandler = (FailedResponse?) -> Boolean
typealias StringHandler = (String) -> Unit

enum class Method {
    GET,
    POST,
    DELETE,
    PUT
}

data class FailedResponse (
    var code: String,
    var msg: String
)

interface AfterParsable {
    fun afterParse(headers: Headers?)
}

fun StringMap.toQueryParam() : String {
    if ( this.size == 0 ) {
        return ""
    }
    return this.keys.map { key ->
        val value = URLEncoder.encode(this[key], "UTF-8")
        "$key=$value"
    }.joinToString("&", "?")
}

abstract class RequestOption {
    constructor(showLoading: Boolean = true, body:Any? = null) {
        this.body = if( body is String) body else (body?.toJson() ?: "")
        if ( body is String ) {
            this.body = body
        } else
        this.showLoading = showLoading
    }

    var body: String? = null
    var showLoading: Boolean = true

    fun body() : String {
        return body ?: ""
    }

    abstract fun endPoint() : String

    open fun queryParamMap() : StringMap {
        return StringMap()
    }

    fun queryParam() : String {
        return queryParamMap().toQueryParam()
    }

    open fun method(): Method {
        return Method.GET
    }

    open fun contentType(): String {
        return "application/json"
    }

    open fun retryCount(): Int {
        return 2
    }
}

interface BaseRequester {
    fun request()
    fun cancel()
    fun option() : RequestOption
    fun url() : String
    fun header() : StringMap
    fun urlCommon(orig: HttpUrl) : HttpUrl.Builder
}

interface Requestable {
    fun addRequester(requester: BaseRequester)
    fun removeRequester(requester: BaseRequester)
    fun showError(error: FailedResponse?, debugMsg: String)
    fun requesters() : List<BaseRequester>
    fun context() : Context?
    fun needLoading() : Boolean
}

abstract class Requester<Res>(
    open var type: Type,
    open var requestable: Requestable,
    open var requestOption: RequestOption,
    open var complete: CompleteHandler<Res>? = null,
    open var failed: FailedHandler? = null,
    open var stringHandler: StringHandler? = null

) : BaseRequester {
    var call: Call<String>? = null
    var failedCount = 0

    companion object {
        val gson = Gson()
    }

    init {
        request()
    }

    abstract override fun url() : String
    abstract override fun header() : StringMap
    abstract override fun urlCommon(orig: HttpUrl) : HttpUrl.Builder

    fun checkLoading() {
        val option = option()

        if( option.showLoading && requestable.needLoading() ){
            if(requestable.requesters().filter { it.option().showLoading }.isEmpty()) {
                LoadingProgress.shared.dismiss(requestable.context())
            }
        }
    }

    override fun request() {
        cancel()

        requestable.addRequester(this)
        val option = option()

        if( option.showLoading && requestable.needLoading()) {
            LoadingProgress.shared.show(requestable.context())
        }

        val method = option.method()
        val endPoint = "${option.endPoint()}${option.queryParam()}"
        val body = option.body()
        val converter = ScalarsConverterFactory.create()
        when( method ) {
            Method.POST -> {
                val service = ServiceBuilder.buildService<PostService>(url(), converter, this)
                call = service.request(endPoint, body)
            }
            Method.GET -> {
                val service = ServiceBuilder.buildService<GetService>(url(), converter, this)
                call = service.request(endPoint)
            }
            Method.PUT -> {
                val service = ServiceBuilder.buildService<PutService>(url(), converter, this)
                call = service.request(endPoint, body)
            }
            Method.DELETE -> {
                val service = ServiceBuilder.buildService<DeleteService>(url(), converter, this)
                call = service.request(endPoint)
            }
        }

        call?.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: retrofit2.Response<String>) {
                if( response.isSuccessful() ) {
                    withDefers {
                        defer {
                            requestable.removeRequester(this@Requester)
                            checkLoading()
                        }
                        if( stringHandler != null ) {
                            stringHandler?.let { handler ->
                                response.body()?.let { body ->
                                    handler(body)
                                }
                            }
                            if( complete == null ) return
                        }

                        val body = gson.fromJson<Res>(response.body(), type)
                        if (body is AfterParsable) {
                            (body as AfterParsable).afterParse(response.headers())
                        } else if (body is ArrayList<*>) {
                            val list = (body as ArrayList<*>)
                            if (list.isNotEmpty() && list.first() is AfterParsable) {
                                (body as ArrayList<*>).forEach { data ->
                                    (data as AfterParsable).afterParse(response.headers())
                                }
                            }
                        }
                        complete?.let { it(body) }
                    }
                } else {
                    try {
                        val gson = Gson()
                        val failed = gson.fromJson(response.errorBody()?.charStream(), FailedResponse::class.java)
                        failedProc(response.code(), failed)
                    } catch (e: Exception) {
                        failedProc(response.code(), FailedResponse("0", ""))
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                failedProc(0, null)
            }
        })
    }

    fun failedProc(code: Int, resp: FailedResponse?) {
        if( retry() ) return
        withDefers {
            defer {
                requestable.removeRequester(this@Requester)
                checkLoading()
            }

            var processed = false
            if ( failed != null ) {
                processed = failed?.let { it(resp) } == true
            }

            if ( processed ) return

            when( code ) {
                500 -> {
                    // TODO: 서버 오류라 결과 값이 없음
                    requestable.showError(FailedResponse("500","internal server error"), option().endPoint())
                    Toast.makeText(requestable.context(), "서버 오류", Toast.LENGTH_SHORT).show()
                }
                401 -> {
                    // TODO: 인증 오류
                    requestable.showError(FailedResponse("401","autorization failed"), option().endPoint())
                    Toast.makeText(requestable.context(), "인증 실패", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    if( resp != null ) {
                        // TODO: 파싱 가능한 오류
                        requestable.showError(resp, option().endPoint())
                        Toast.makeText(requestable.context(), "${resp.code}: ${resp.msg}", Toast.LENGTH_SHORT).show()
                    } else {
                        // TODO: else 공통 네트워크 오류

                        requestable.showError(FailedResponse("0","network error"), option().endPoint())
                        Toast.makeText(requestable.context(), "그냥 오류", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun retry(): Boolean {
        failedCount += 1
        if ( failedCount >= option().retryCount() ) {
            return false
        } else {
            requestable.removeRequester(this)
            request()
            return true
        }
    }

    override fun cancel() {
        if( call != null ) {
            call?.cancel()
        }
    }

    override fun option(): RequestOption {
        return requestOption
    }
}

