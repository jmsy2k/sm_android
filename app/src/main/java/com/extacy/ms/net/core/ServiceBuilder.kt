package com.extacy.ms.net.core

import com.extacy.ms.BuildConfig
import okhttp3.*
import okhttp3.Headers.Companion.toHeaders
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.Exception
import java.lang.RuntimeException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.collections.HashMap

typealias StringMap = HashMap<String, String>

fun StringMap.added(headers: StringMap): StringMap {
    val temp = this.clone() as? StringMap ?: StringMap()
    temp.putAll(headers)
    return temp
}

fun Headers.toMap(): StringMap {
    var result = HashMap<String, String>()
    for( i in 0 until size ) {
        result.put(name(i), value(i))
    }
    return result
}

class ServiceBuilder {
    class Header {
        companion object {

            fun genHeader(orig: StringMap, custom: StringMap): Headers {
                return orig.added(custom).toHeaders()
            }
        }
    }

    companion object {
        inline fun <reified T> buildService(url: String, converter: Converter.Factory, requester: BaseRequester) : T {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                .connectTimeout(10L, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val original = chain.request()
                    val headerAdded: Request.Builder = original.newBuilder()
                    headerAdded.headers(
                        Header.genHeader(original.headers.toMap(), requester.header())
                    )
                    val builder = requester.urlCommon(original.url)

                    headerAdded.url(builder.build())
                    chain.proceed(headerAdded.build())
                })


            try {
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                })

                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                val sslSocketFactory = sslContext.socketFactory

                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier { _, _ -> true }

            } catch (e: Exception) {
                throw RuntimeException(e)
            }

            val client = builder.build()

            return Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(converter)
                .build()
                .create(T::class.java)
        }
    }
}

