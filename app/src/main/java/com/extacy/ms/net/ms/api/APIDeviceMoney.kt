package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS

object APIDeviceMoney {
    fun request(requestable: Requestable,
                deviceId: Int,
                startTm: Long,
                endTm: Long,
                page: Int,
                complete: CompleteHandler<DS<MoneyList>>
    ) {
        var body = HashMap<String, Any>()
        body["device_id"] = deviceId
        body["start_tm"] = startTm
        body["end_tm"] = endTm
        body["page"] = page

        MSRequester.request(requestable, object: RequestOption(body = body) {
            override fun endPoint(): String {
                return "device_money"
            }

            override fun method(): Method {
                return Method.POST
            }
        }, complete)
    }
}
