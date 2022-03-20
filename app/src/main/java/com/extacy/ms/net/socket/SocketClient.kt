package com.extacy.ms.net.socket

import android.util.Log
import com.extacy.ms.common.Common
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*

import java.lang.Exception
import java.net.Socket
import java.nio.ByteBuffer

//@Suppress("UNCHECKED_CAST")
//fun <T : Serializable> fromByteArray(byteArray: ByteArray): T {
//    val byteArrayInputStream = ByteArrayInputStream(byteArray)
//    val objectInput: ObjectInput
//    objectInput = ObjectInputStream(byteArrayInputStream)
//    val result = objectInput.readObject() as T
//    objectInput.close()
//    byteArrayInputStream.close()
//    return result
//}
//
//fun Serializable.toByteArray(): ByteArray {
//    val byteArrayOutputStream = ByteArrayOutputStream()
//    val objectOutputStream: ObjectOutputStream
//    objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
//    objectOutputStream.writeObject(this)
//    objectOutputStream.flush()
//    val result = byteArrayOutputStream.toByteArray()
//    byteArrayOutputStream.close()
//    objectOutputStream.close()
//    return result
//}

val SocketURL = "192.168.123.101"
val SocketPORT: Int = 25000

class Test : Serializable {
    var testShort: Short = 2
    var testBool: Boolean = true
    var testInt: Int = 4
}

object SocketClient {


    inline fun <reified T: SocketResponse> request(body: SocketRequest, classT:Class<T>,  crossinline response:(T?, Exception?) -> Unit): Boolean {


        CoroutineScope(Dispatchers.IO).launch {
            var socket: Socket? = null
            var outStream: OutputStream? = null
            var inStream: InputStream?  = null
            try {
                socket = Socket(SocketURL, SocketPORT)

                outStream = socket.outputStream
                inStream = socket.inputStream




                var bodyBytes = ReqConnect(Common.userInfo.id).toByteArray()
                outStream.write(bodyBytes)
                var resByteArray = ByteArray(4096)

                inStream.read(resByteArray)


                bodyBytes = body.toByteArray()
                outStream.write(bodyBytes)

                inStream.read(resByteArray)


                val res = classT.newInstance()

                res.parse(ByteBuffer.wrap(resByteArray))

    //                    val res: T = fromByteArray(resByteArray)
                CoroutineScope(Dispatchers.Main).launch {
                    response(res, null)
                }

//                val available = inStream.available()
//                if (available > 0){

//                }
            } catch ( e: Exception ) {
                CoroutineScope(Dispatchers.Main).launch {
                    response(null, e)
                }

                Log.e("ms", e.toString())
            } finally {

                outStream?.close()
                inStream?.close()

                socket?.close()
            }
        }
        return true
    }
}