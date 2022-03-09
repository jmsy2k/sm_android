package com.extacy.ms

import android.os.Bundle
import com.extacy.ms.databinding.ActivityMainBinding
import com.extacy.ms.base.ViewBindingActivity
import com.extacy.ms.scene.login.LoginFragment

class MainActivity : ViewBindingActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment = LoginFragment()
        replaceFragment(fragment)

    }
}