package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS
import com.extacy.ms.net.ms.ds.ResLogin

class ManagerType {
    var id = 0
    var name_ko = ""
}

object APIManagerType {
    fun request(requestable: Requestable,
                complete: CompleteHandler<DS<List<ManagerType>>>) {

        MSRequester.request(requestable, object: RequestOption() {
            override fun endPoint(): String {
                return "manager_type"
            }

            override fun method(): Method {
                return Method.POST
            }
        }, complete)
    }
}