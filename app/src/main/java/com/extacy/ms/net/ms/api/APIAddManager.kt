package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS
import com.extacy.ms.net.ms.ds.ResEmpty
import com.extacy.ms.net.ms.ds.ResLogin


object APIAddManager {
    fun request(requestable: Requestable,
                id: String,
                pw: String,
                name: String,
                type: Int,
                complete: CompleteHandler<DS<ResEmpty>>) {


        var body = HashMap<String, Any>()

        body["user_id"] = id
        body["password"] = pw
        body["user_name"] = name
        body["type_id"] = type

        MSRequester.request(requestable, object: RequestOption(body = body) {
            override fun endPoint(): String {
                return "add_manager"
            }

            override fun method(): Method {
                return Method.POST
            }
        }, complete)
    }
}