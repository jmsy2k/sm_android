package com.extacy.ms.net.ms.api

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.Method
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable
import com.extacy.ms.net.ms.MSRequester
import com.extacy.ms.net.ms.ds.DS
import com.extacy.ms.net.ms.ds.ResponseBody

class GameEvent {
    var id = 0
    var name = ""
    var vk = 0
}
class GameInfo {
    var name = ""
    var events: MutableList<GameEvent> = mutableListOf()
}
object APIGameInfo {
    fun request(requestable: Requestable,
                id: Int,
                complete: CompleteHandler<DS<GameInfo>>
    ) {
        var body = HashMap<String, Any>()
        body["game_id"] = id

        MSRequester.request(requestable, object: RequestOption(body = body) {
            override fun endPoint(): String {
                return "game_info"
            }

            override fun method(): Method {
                return Method.POST
            }
        }, complete)
    }
}