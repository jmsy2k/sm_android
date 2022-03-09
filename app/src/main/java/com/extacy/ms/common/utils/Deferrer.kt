package com.extacy.ms.common.utils

class Deferrer {
    private val actions = arrayListOf<() -> Unit>()

    fun defer(f: () -> Unit) {
        actions.add(f)
    }

    fun done() {
        actions.reverse()
        for (action in actions) {
            action()
        }
    }
}

inline fun <T> withDefers(body: Deferrer.() -> T): T {
    val deferrer = Deferrer()
    val result = deferrer.body()
    deferrer.done()
    return result
}
