package com.extacy.ms.net.socket

import com.extacy.ms.net.socket.SocketPacket.A2S_CALL
import com.extacy.ms.net.socket.SocketPacket.A2S_DEVICE_LIST
import com.extacy.ms.net.socket.SocketPacket.A2S_STORE_IS_ONLINE
import com.extacy.ms.net.socket.SocketPacket.CONNECT
import com.extacy.ms.net.socket.SocketPacket.S2A_STORE_IS_ONLINE
import java.io.Serializable
import java.nio.ByteBuffer
import java.nio.ByteOrder


object SocketPacket {
    val CONNECT: Short = 5000
    val CONNECTED: Short = 5001
    // A <-> S
    val A2S_STORE_IS_ONLINE: Short = 30020
    val S2A_STORE_IS_ONLINE: Short = 30021
    val A2S_DEVICE_LIST: Short = 30010
    val S2A_DEVICE_LIST: Short = 30011
    val A2S_CALL: Short = 30050
    val S2A_CALL: Short = 30051
}

val HEADER_SIZE: Short = 5


open class Header: Serializable {
    var packetCode: Short = 0
    var type: Byte = 100

    fun genByteBuffer(capacity: Int): ByteBuffer {
        val byteBuffer = ByteBuffer.allocate(capacity)
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
        byteBuffer.putShort(capacity.toShort())
        byteBuffer.putShort(packetCode)
        byteBuffer.put(type)
        return byteBuffer
    }

    fun parseHeader(byteBuffer: ByteBuffer) {
        val size = byteBuffer.getShort()
        packetCode = byteBuffer.getShort()
        type = byteBuffer.get()
    }
}

open abstract class SocketRequest(packetCode: Short): Header() {
    abstract fun bodySize(): Short
    abstract fun fillBody(byteBuffer: ByteBuffer)

    init {
        this.packetCode = packetCode
    }

    fun toByteArray(): ByteArray {
        val capacity = HEADER_SIZE + bodySize()
        var buffer = genByteBuffer(capacity)
        buffer.getShort(1)
        fillBody(buffer)
        return buffer.array()
    }
}

open abstract class SocketResponse: Header() {
    abstract fun parseBody(byteBuffer: ByteBuffer)

    fun parse(byteBuffer: ByteBuffer) {
        parseHeader(byteBuffer)
        parseBody(byteBuffer)
    }
}

class ReqConnect(managerId: Int): SocketRequest(CONNECT) {
    var managerId: Int = 0
    init {
        this.managerId = managerId
    }

    override fun bodySize(): Short {
        return 4
    }

    override fun fillBody(byteBuffer: ByteBuffer) {
        byteBuffer.putInt(managerId)
    }
}

class ReqStoreIsOnline(storeId: Int): SocketRequest(A2S_STORE_IS_ONLINE) {
    var storeId: Int = 0
    init {
        this.storeId = storeId

    }
    override fun bodySize(): Short {
        return 4
    }

    override fun fillBody(byteBuffer: ByteBuffer) {
        byteBuffer.putInt(storeId)
    }
}

class ResStoreIsOnline: SocketResponse() { //
    var isOnline: Boolean = false
    override fun parseBody(byteBuffer: ByteBuffer) {
        val b = byteBuffer.get()
        isOnline = b == 1.toByte()
    }
}

class ReqDeviceList(storeId: Int): SocketRequest(A2S_DEVICE_LIST) {
    var storeId: Int = 0
    init {
        this.storeId = storeId
    }
    override fun bodySize(): Short {
        return 4
    }

    override fun fillBody(byteBuffer: ByteBuffer) {
        byteBuffer.putInt(storeId)
    }
}

class ResDeviceList: SocketResponse() {
    var deviceList: MutableList<Int> = mutableListOf()
    override fun parseBody(byteBuffer: ByteBuffer) {
        val size = byteBuffer.getShort()
        for( i in 0 until size) {
            deviceList.add(byteBuffer.getInt())
        }
    }
}

class ReqCall(storeId: Int, deviceId: Int, vk: Int): SocketRequest(A2S_CALL) {
    var storeId: Int = 0
    var deviceId: Int = 0
    var vk: Int = 0

    init {
        this.storeId = storeId
        this.deviceId = deviceId
        this.vk = vk
    }
    override fun bodySize(): Short {
        return 12
    }

    override fun fillBody(byteBuffer: ByteBuffer) {
        byteBuffer.putInt(storeId)
        byteBuffer.putInt(deviceId)
        byteBuffer.putInt(vk)
    }
}

class ResCall: SocketResponse() {
    var code = 0
    override fun parseBody(byteBuffer: ByteBuffer) {
        code = byteBuffer.getInt()
    }
}