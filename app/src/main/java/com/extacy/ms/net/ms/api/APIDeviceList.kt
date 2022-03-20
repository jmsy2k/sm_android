package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS
import com.extacy.ms.net.ms.ds.ResponseBody

class DeviceItem {
    class GameItem {
        var id = 0
        var name = ""
    }
    var id = 0
    var name = ""
    var mac_addr = ""
    var game: GameItem = GameItem()
}

object APIDeviceList {
    fun request(requestable: Requestable,
                storeId: Int,
                complete: CompleteHandler<DS<MutableList<DeviceItem>>>
    ) {
        var body = HashMap<String, Any>()
        body["store_id"] = storeId
        MSRequester.request(requestable, object: RequestOption(body = body) {
            override fun endPoint(): String {
                return "device_list"
            }

            override fun method(): Method {
                return Method.POST
            }
        }, complete)
    }
}