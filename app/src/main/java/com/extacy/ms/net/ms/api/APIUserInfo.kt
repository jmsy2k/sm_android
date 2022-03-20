package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS
import com.extacy.ms.net.ms.ds.ResLogin

class IdOnly {
    var id = 0
}
class UserInfo {
    var id = 0
    var user_name = ""
    var manager_type: IdOnly = IdOnly()
}

object APIUserInfo {
    fun request(requestable: Requestable,
                complete: CompleteHandler<DS<UserInfo>>) {
        MSRequester.request(requestable, object: RequestOption() {
            override fun endPoint(): String {
                return "user_info"
            }

            override fun method(): Method {
                return Method.POST
            }
        }, complete)
    }
}