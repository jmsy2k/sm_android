package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS
import com.extacy.ms.net.ms.ds.ResEmpty

//object APIStoreInfo {
//    fun request(requestable: Requestable,
//                id: String,
//                startTm: Long,
//                endTm: Long,
//                complete: CompleteHandler<DS<다른값들 집어 넣고 테스트 ㄱㄱ>) {
//
//
//        var body = HashMap<String, Any>()
//
//        body["store_id"] = id
//        body["start_tm"] = startTm
//        body["end_tm"] = endTm
//
//        MSRequester.request(requestable, object: RequestOption(body = body) {
//            override fun endPoint(): String {
//                return "store_info"
//            }
//
//            override fun method(): Method {
//                return Method.POST
//            }
//        }, complete)
//    }
//}