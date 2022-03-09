package com.extacy.ms

import androidx.multidex.MultiDexApplication
import com.extacy.ms.common.Common

class MSApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        Common.initialze(applicationContext)
    }
}