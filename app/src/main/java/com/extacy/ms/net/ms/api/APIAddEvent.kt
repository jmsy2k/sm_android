package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS
import com.extacy.ms.net.ms.ds.ResEmpty

object APIAddEvent {
    fun request(requestable: Requestable,
                game_id: Int,
                name: String,
                vk: Int,
                complete: CompleteHandler<DS<ResEmpty>>
    ) {
        var body = HashMap<String, Any>()
        body["game_id"] = game_id
        body["name"] = name
        body["vk"] = vk
        MSRequester.request(requestable, object: RequestOption(body = body) {
            override fun endPoint(): String {
                return "add_event"
            }

            override fun method(): Method {
                return Method.POST
            }
        }, complete)
    }
}