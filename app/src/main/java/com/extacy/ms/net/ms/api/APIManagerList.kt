package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS
import com.extacy.ms.net.ms.ds.ResponseBody


class ManagerInfo {
    class ManagerType {
        var id = 0
        var name_ko = ""
    }
    var id = 0
    var user_id = ""
    var user_name = ""
    var creater_name = ""
    var is_block = false

    var manager_type = ManagerType()
}
class ManagerList: ResponseBody(){
    var managers: MutableList<ManagerInfo> = mutableListOf()
    var has_next = false
    var keyword = ""
    var page = 0
}

object APIManagerList {
    fun request(requestable: Requestable,
                page: Int,
                keyword: String,
                complete: CompleteHandler<DS<ManagerList>>
    ) {
        var body = HashMap<String, Any>()
        body["page"] = page
        body["keyword"] = keyword

        MSRequester.request(requestable, object: RequestOption(body = body) {
            override fun endPoint(): String {
                return "manager_list"
            }

            override fun method(): Method {
                return Method.POST
            }
        }, complete)
    }
}