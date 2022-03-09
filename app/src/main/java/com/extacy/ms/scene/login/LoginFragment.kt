package com.extacy.ms.scene.login

import android.os.Bundle
import android.view.View
import com.extacy.ms.base.ViewBindingFragment
import com.extacy.ms.common.Pref
import com.extacy.ms.databinding.FragmentLoginBinding
import com.extacy.ms.net.ms.api.APILogin
import com.extacy.ms.scene.main.MainFragment


class LoginFragment: ViewBindingFragment<FragmentLoginBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editId.setText(Pref.lastLoginId)

        binding.buttonLogin.setOnClickListener {
            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()

            if( id.isEmpty() ) {
                showAlert("no id")
                return@setOnClickListener
            }

            if( pw.isEmpty()) {
                showAlert("no pw")
                return@setOnClickListener
            }
            Pref.lastLoginId = binding.editId.text.toString()
            APILogin.request(requestable, id, pw) { res ->
                if (res.isSuccess()) {
                    val body = res.body
                    val token = body?.token //res.body(ResLogin::class.java)?.token
                    if (!token.isNullOrEmpty()) {
                        Pref.token = token
                        val fragment = MainFragment()
                        replaceFragment(fragment)
                    } else {
                        showAlert("fail login: no token")
                    }
                } else {
                    showAlert("fail login: ${res.msg}")
                }
            }
        }
    }
}