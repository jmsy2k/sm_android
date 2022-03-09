package com.extacy.ms.common

import android.content.Context
import com.extacy.ms.net.UrlSelector

class Common {
    companion object {
        lateinit var appContext: Context
        lateinit var serviceContext: Context


        val isServiceInitialize : Boolean
            get() = this::serviceContext.isInitialized

        var userAgent: String = ""
        var versionName: String = ""
        fun initialze(appContext: Context) {
            Companion.appContext = appContext
            UrlSelector.initialize()
            versionName = appContext.packageManager.getPackageInfo(appContext.packageName, 0).versionName
        }


    }
}


const val PLAYLIST_ID = "PLAYLIST_ID"
const val NOW_PLAYING = "NOW_PLAYING"
const val MEDIA_QUEUE_POSITION = "MEDIA_QUEUE_POSITION"
const val SEEK_BAR_PROGRESS = "SEEK_BAR_PROGRESS"
const val MEDIA_SAVE_QUEUE_POSITION = "MEDIA_SAVE_QUEUE_POSITION"
const val PLAYLIST_IDENTIFIER = "PLAYLIST_IDENTIFIER"
const val EMPTY_MEDIA = "EMPTY_MEDIA"
const val LAST_CATEGORY = "LAST_CATEGORY"
const val LAST_ARTIST = "LAST_ARTIST"
const val LAST_ARTIST_IMAGE = "LAST_ARTIST_IMAGE"
const val SEEK_BAR_MAX = "SEEK_BAR_MAX"