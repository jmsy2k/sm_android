package com.extacy.ms.net.ms.ds

import com.extacy.ms.extensions.toJson
import com.extacy.ms.net.core.AfterParsable
import com.google.gson.Gson

open class ResEmpty: ResponseBody() {

}
open class DS<T> {
    var code = 0
    var msg = ""
    var body: T? = null

//    inline fun <reified T> body(classT: Class<T>): T? {
//        val gson = Gson()
//        return gson.fromJson(body?.toJson(), classT)
//    }
//
//    inline fun <reified T> body(): T? {
//        val gson = Gson()
//        return gson.fromJson(body?.toJson(), T::class.java)
//    }

    fun isSuccess(): Boolean {
        return code == 200
    }
}