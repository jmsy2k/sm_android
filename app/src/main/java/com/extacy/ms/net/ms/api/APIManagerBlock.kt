package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS
import com.extacy.ms.net.ms.ds.ResEmpty

object APIManagerBlock {
    fun request(requestable: Requestable,
                id: Int,
                isBlock: Boolean,
                complete: CompleteHandler<DS<ResEmpty>>) {
        var body = HashMap<String, Any>()

        body["managerId"] = id
        body["isBlock"] = isBlock

        MSRequester.request(requestable, object: RequestOption(body = body) {
            override fun endPoint(): String {
                return "manager_block"
            }

            override fun method(): Method {
                return Method.POST
            }
        }, complete)
    }
}