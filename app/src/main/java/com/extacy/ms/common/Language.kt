package com.extacy.ms.common

import java.util.*

class Language {
    companion object {
        fun get(): String {
            // Get current language.
            var language = Locale.getDefault().language
            if (Locale.KOREAN.language != language) language = Locale.ENGLISH.language
            return language.uppercase(Locale.US)
        }
    }
}