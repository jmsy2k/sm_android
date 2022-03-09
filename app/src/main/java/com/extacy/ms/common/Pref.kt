package com.extacy.ms.common

import android.content.Context
import com.extacy.ms.extensions.toJson
import com.extacy.ms.net.UrlSelector
import com.extacy.ms.net.core.Requester.Companion.gson

import java.util.*
import kotlin.collections.ArrayList

object Pref {

    enum class Key {
        Token,
        LastLoginId,
        DeviceUuid,
        ForceMediaLocation,
        MediaLocation,
        TestCountry,
        UserCountry,
        SelectedServer,
        CustomApiDomain,
        CustomWebDomain,
        PlayingChannel,
        RecentSearch,
    }

    const val NAME = "Pref"

    var token: String
        get() = get(Key.Token.name, "")
        set(value) = set(value, Key.Token.name)

    var lastLoginId: String
        get() = get(Key.LastLoginId.name, "")
        set(value) = set(value, Key.LastLoginId.name)

    var deviceUuid: String
        get() {
            var res = get(Key.DeviceUuid.name, "")
            if ( res.isEmpty() ) {
                res = UUID.randomUUID().toString()
                deviceUuid = res
            }
            return res
        }
        set(value) = set(value, Key.DeviceUuid.name)

    var mediaLocation: MediaLocation = MediaLocation.Auto
        get() {
            if( field != null ) {
                return field
            }

            var res = MediaLocation.valueOf(get(Key.ForceMediaLocation.name, MediaLocation.Auto.key))
            if( res == MediaLocation.Auto) {
                res = MediaLocation.valueOf(get(Key.MediaLocation.name, MediaLocation.Virginia.key))
            }
            field = res
            return res

        }
        set(value) {
            field = value
            set(value.key, Key.MediaLocation.name)
        }

    var userCountry: String
        get() {
            if( isTestCountry ){
                return "VD"
            }
            return get(Key.UserCountry.name, "")
        }
        set(value) = set(value, Key.UserCountry.name)

    var isTestCountry: Boolean
        get() = get(Key.TestCountry.name, false)
        set(value) = set(value, Key.TestCountry.name)

    var selectedServer: Int
        get() = get(Key.SelectedServer.name, 0)
        set(value) = set(value, Key.SelectedServer.name)

    var customApiDomain: String
        get() = get(Key.CustomApiDomain.name, UrlSelector.DEV_API)
        set(value) = set(UrlSelector.checkCustomUrl(value), Key.CustomApiDomain.name)

    var customWebDomain: String
        get() = get(Key.CustomWebDomain.name, UrlSelector.DEV_WEB)
        set(value) = set(UrlSelector.checkCustomUrl(value), Key.CustomWebDomain.name)

    var playingChannel: String
        get() = get(Key.PlayingChannel.name, "")
        set(value) = set(value, Key.PlayingChannel.name)


    var recentSearch: ArrayList<String> = ArrayList()
        get() {
            if( field.size > 0 ) {
                return field
            }
            val str = get(Key.RecentSearch.name, "[]")

            val array = gson.fromJson(str, Array<String>::class.java)
            array.forEach {
                field.add(it)
            }

            return field
        }
        set(value) {
            field = value
            var str = value.toJson()
            set(str, Key.RecentSearch.name)
        }

    inline fun <reified T> set(value: T, key: String) {
        set(value, key, NAME)
    }

    inline fun <reified T> set(value: T, key: String, name: String) {
        setCache(value, key)
        val edit = Common.appContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit()
        when (value) {
            is String  -> {
                edit.putString(key, value)
            }
            is Long    -> {
                edit.putLong(key, value)
            }
            is Int     -> {
                edit.putInt(key, value)
            }
            is Float   -> {
                edit.putFloat(key, value)
            }
            is Boolean -> {
                edit.putBoolean(key, value)
            }
        }
        edit.apply()
    }

    private fun clearPref(name: String = NAME) {
        val edit = Common.appContext.getSharedPreferences(name, Context.MODE_PRIVATE).edit()
        edit.clear()
        edit.apply()
    }

    fun resetPref() {
        clearPref(NAME)
        cacheMap.clear()
    }

    inline fun <reified T> get(key: String, def: T): T {
        return get(key, NAME, def)
    }

    inline fun <reified T> get(key: String, name: String, def: T): T {
        val cache = getCache(key) as? T
        if (cache != null) {
            return cache
        }

        val pref = Common.appContext.getSharedPreferences(name, Context.MODE_PRIVATE)
        var value: T? = null
        when (def) {
            is String  -> {
                value = pref.getString(key, def) as T
            }
            is Long    -> {
                value = pref.getLong(key, def) as T
            }
            is Int     -> {
                value = pref.getInt(key, def) as T
            }
            is Float   -> {
                value = pref.getFloat(key, def) as T
            }
            is Boolean -> {
                value = pref.getBoolean(key, def) as T
            }
        }
        val result = value ?: def
        setCache(result, key)
        return result
    }

    private val cacheMap = mutableMapOf<String, Any?>()
    fun getCache(key: String): Any? {
        return cacheMap[key]
    }

    fun setCache(value: Any?, key: String) {
        cacheMap[key] = value
    }
}
