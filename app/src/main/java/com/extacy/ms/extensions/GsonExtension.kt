package com.extacy.ms.extensions

import com.google.gson.Gson
import java.lang.reflect.Type

var gson = Gson()
fun Any.toJson(): String {

    return gson.toJson(this)
}

fun <T> String.toJsonModel(type: Type): T {
    return gson.fromJson(this, type)
}