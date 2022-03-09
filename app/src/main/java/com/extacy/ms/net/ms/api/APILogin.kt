package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS
import com.extacy.ms.net.ms.ds.ResLogin
import com.google.gson.reflect.TypeToken

object APILogin {
    fun request(requestable: Requestable,
        id: String,
        pw: String,
        complete: CompleteHandler<DS<ResLogin>>) {


        var body = HashMap<String, String>()

        body["user_id"] = id
        body["password"] = pw

        MSRequester.request(requestable, object: RequestOption(body = body) {
            override fun endPoint(): String {
                return "login"
            }

            override fun method(): Method {
                return Method.POST
            }
        }, complete)
    }
}