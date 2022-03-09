package com.extacy.ms.net.ms

import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.extacy.ms.BuildConfig
import com.extacy.ms.common.Common
import com.extacy.ms.common.Language
import com.extacy.ms.common.Pref
import com.extacy.ms.net.UrlSelector
import com.extacy.ms.net.core.*
import com.google.gson.reflect.TypeToken
import okhttp3.HttpUrl
import java.lang.reflect.Type
import java.util.*

class MSRequester<Res>(type: Type,
                       sender: Requestable,
                       requestOption: RequestOption,
                       complete: CompleteHandler<Res>? = null,
                       failed: FailedHandler? = null,
                       stringHandler: StringHandler? = null)
    : Requester<Res>(type, sender, requestOption, complete, failed, stringHandler) {



    companion object {
        val default: StringMap = object: HashMap<String, String>() {
            init {
                put("User-Agent", Common.userAgent)
                put("device-manufacturer", Build.MANUFACTURER)
                put("device-model", Build.MODEL)
                put("os-name", "Android")
                put("os-version", Build.VERSION.RELEASE)
                put("app-version", BuildConfig.VERSION_NAME)
                put("app-version-code", "${BuildConfig.VERSION_CODE}")
            }
        }

        fun mutable(): StringMap {
            return object: HashMap<String, String>() {
                init {
                    put("user-locale", Locale.getDefault().language)
                    put("user-time-zone", TimeZone.getDefault().id)
                    put("duid", Pref.deviceUuid)
                    put("media-location", Pref.mediaLocation.key)
                    put("token", Pref.token)
                }
            }
        }

        inline fun <reified T> request(sender: Requestable,
                                       option: RequestOption,
                                       noinline complete: CompleteHandler<T>,
                                       noinline failed: FailedHandler,
                                       noinline stringHandler: StringHandler) {
            MSRequester(object: TypeToken<T>() {}.type,
                sender,
                option,
                complete,
                failed,
                stringHandler
            )
        }

        inline fun <reified T> request(sender: Requestable,
                                       option: RequestOption,
                                       noinline complete: CompleteHandler<T>,
                                       noinline failed: FailedHandler) {
            MSRequester(object: TypeToken<T>() {}.type,
                sender,
                option,
                complete,
                failed
            )
        }

        inline fun <reified T> request(sender: Requestable,
                                       option: RequestOption,
                                       noinline complete: CompleteHandler<T>) {
            MSRequester(object: TypeToken<T>() {}.type,
                sender,
                option,
                complete
            )
        }
    }

    override fun url(): String {
        return UrlSelector.api
    }

    override fun header(): StringMap {
        val header = default.added(mutable())
        if (Pref.userCountry.isNotEmpty()) {
            header.put("user-country", Pref.userCountry)
        }
        return header
    }

    override fun urlCommon(orig: HttpUrl) : HttpUrl.Builder {
        val builder = orig.newBuilder()

        val language: String = Language.get()
        if (BuildConfig.DEBUG) Log.d("OkHttp", "Language: $language")

        builder.addQueryParameter("lang", language)

        if (TextUtils.isEmpty(orig.queryParameter("user_lang"))) {
            builder.addQueryParameter("user_lang", Locale.getDefault().language)
        }

        if (!TextUtils.isEmpty(Pref.userCountry)) {
            builder.addQueryParameter("country", Pref.userCountry)
        }
        return builder
    }
}
