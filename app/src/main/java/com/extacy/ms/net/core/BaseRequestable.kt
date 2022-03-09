package com.extacy.ms.net.core

import android.content.Context

abstract class BaseRequestable : Requestable {
    var requesters : ArrayList<BaseRequester> = ArrayList()
    override fun addRequester(requester: BaseRequester) {
        requesters.add(requester)
    }

    override fun removeRequester(requester: BaseRequester) {
        requesters.remove(requester)
    }

    override fun showError(error: FailedResponse?, debugMsg: String) {

    }

    override fun requesters(): List<BaseRequester> {
        return requesters
    }

    override fun needLoading(): Boolean {
        return true
    }

    abstract override fun context(): Context?
}