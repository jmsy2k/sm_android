package com.extacy.ms.net.test

import com.extacy.ms.net.core.*
import com.extacy.ms.net.core.StringMap
import com.extacy.ms.net.ms.MSRequester
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl
import java.lang.reflect.Type

class TestRequester<Res>(type: Type,
                         requestable: Requestable,
                         requestOption: RequestOption,
                         complete: CompleteHandler<Res>? = null,
                         failed: FailedHandler? = null,
                         stringHandler: StringHandler? = null)
    : Requester<Res>(type, requestable, requestOption, complete, failed, stringHandler) {

    companion object {
        inline fun <reified T> request(requestable: Requestable,
                                       option: RequestOption,
                                       noinline complete: CompleteHandler<T>,
                                       noinline failed: FailedHandler,
                                       noinline stringHandler: StringHandler
        ) {
            TestRequester(object: TypeToken<T>() {}.type,
                requestable,
                option,
                complete,
                failed,
                stringHandler
            )

        }

        inline fun <reified T> request(requestable: Requestable,
                                       option: RequestOption,
                                       noinline complete: CompleteHandler<T>,
                                       noinline failed: FailedHandler
        ) {
            TestRequester(object: TypeToken<T>() {}.type,
                requestable,
                option,
                complete,
                failed
            )

        }

        inline fun <reified T> request(requestable: Requestable,
                                       option: RequestOption,
                                       noinline complete: CompleteHandler<T>
        ) {

            TestRequester(object: TypeToken<T>() {}.type,
                requestable,
                option,
                complete
            )
        }
    }
    override fun url(): String {
        return "https://api.androidhive.info/"
    }

    override fun header(): StringMap {
        return StringMap()
    }

    override fun urlCommon(orig: HttpUrl): HttpUrl.Builder {
        return orig.newBuilder()
    }
}