package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS
import com.extacy.ms.net.ms.ds.ResponseBody


class MoneyItem {
    class Device {
        var id = 0
        var name = ""
        var mac_addr = ""
    }
    var id = 0
    var money = 0
    var dt: Long = 0
    var device: Device = Device()
}

class MoneyList: ResponseBody(){
    var moneys: MutableList<MoneyItem> = mutableListOf()
    var has_next = false
    var total = 0
    var page = 0
}

object APIStoreMoney {
    fun request(requestable: Requestable,
                storeId: Int,
                startTm: Long,
                endTm: Long,
                page: Int,
                complete: CompleteHandler<DS<MoneyList>>
    ) {
        var body = HashMap<String, Any>()
        body["store_id"] = storeId
        body["start_tm"] = startTm
        body["end_tm"] = endTm
        body["page"] = page

        MSRequester.request(requestable, object: RequestOption(body = body) {
            override fun endPoint(): String {
                return "store_money"
            }

            override fun method(): Method {
                return Method.POST
            }
        }, complete)
    }
}
