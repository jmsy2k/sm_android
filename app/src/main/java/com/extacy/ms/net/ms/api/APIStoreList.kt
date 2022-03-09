package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS
import com.extacy.ms.net.ms.ds.ResponseBody

class StoreItem {
    var id = 0
    var name = ""
}

class StoreList: ResponseBody(){
    var stores: MutableList<StoreItem> = mutableListOf()
    var has_next = false
    var keyword = ""
    var page = 0
}

object APIStoreList {
    fun request(requestable: Requestable,
                                   page: Int,
                                   keyword: String,
                                   complete: CompleteHandler<DS<StoreList>>
    ) {
        var body = HashMap<String, Any>()
        body["page"] = page
        body["keyword"] = keyword

        MSRequester.request(requestable, object: RequestOption(body = body) {
            override fun endPoint(): String {
                return "store_list"
            }

            override fun method(): Method {
                return Method.POST
            }
        }, complete)
    }
}