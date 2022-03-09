package com.extacy.ms.net.test

import com.extacy.ms.net.core.CompleteHandler
import com.extacy.ms.net.core.RequestOption
import com.extacy.ms.net.core.Requestable

object APITest {
    fun request(requestable: Requestable,
                                   complete: CompleteHandler<TestData>
    ) {
        TestRequester.request(requestable, object: RequestOption() {
            override fun endPoint(): String {
                return "contacts/"
            }
        }, complete)
    }
}