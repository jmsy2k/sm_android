package com.extacy.ms.net

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import com.extacy.ms.common.Pref
import com.extacy.ms.common.utils.DPIUtil
import android.widget.EditText

import android.widget.LinearLayout
import com.extacy.ms.BuildConfig


data class ServerInfo (val name: String, val api: String, val web: String, val static: String) {

}
object UrlSelector {
    private val PROD_API = "http://192.168.123.100:3000/"
    private val PROD_WEB = "https://m.naver.com/"
    private val PROD_STATIC = "https://m.naver.com/"

    private val STG_API = "http://192.168.123.100:3000/"
    private val STG_WEB = "https://m.naver.com/"
    private val STG_STATIC = "https://m.naver.com/"

    val DEV_API = "http://192.168.123.100:3000/"
    val DEV_WEB = "https://m.naver.com/"
    private val DEV_STATIC = "https://m.naver.com/"

    val name : String
        get() = if( BuildConfig.DEBUG ) selectedServer.name ?: "" else ""
    val api : String
        get() = if( BuildConfig.DEBUG ) selectedServer.api else PROD_API
    val web : String
        get() = if( BuildConfig.DEBUG ) selectedServer.web else PROD_WEB
    val static : String
        get() = if( BuildConfig.DEBUG ) selectedServer.static else PROD_STATIC

    enum class Type {
        dev,
        staging,
        prodcut,
        custom
    }
    lateinit var selectedServer: ServerInfo

    val serverList by lazy {
        arrayListOf (
            ServerInfo(Type.dev.name,
                PROD_API,
                PROD_WEB,
                PROD_STATIC),

            ServerInfo(Type.staging.name,
                STG_API,
                STG_WEB,
                STG_STATIC),

            ServerInfo(Type.prodcut.name,
                DEV_API,
                DEV_WEB,
                DEV_STATIC),

            ServerInfo(Type.custom.name,
                Pref.customApiDomain,
                Pref.customWebDomain,
                DEV_STATIC)
        )
    }

    fun initialize() {
        // 디버그 빌드 일때만
        if( BuildConfig.DEBUG ) {
            val index = Pref.selectedServer
            selectedServer = serverList.getOrNull(index) ?: serverList[0]

        }
    }

    fun checkCustomUrl(input: String) : String {
        var res = input
        if( !res.startsWith("http") ) res = "https://${res}"
        if( !res.endsWith("/") ) res += "/"
        return res
    }

    fun serverSelectViewSetting(activity: Activity, parent: FrameLayout) {
        val button = Button(activity)
        button.text = selectedServer.name

        button.setBackgroundColor(0x00000000)
        button.setTextColor(Color.parseColor("#ffff0000"))

        val layoutParams = FrameLayout.LayoutParams(
            DPIUtil.dp2px(100f).toInt(),
            DPIUtil.dp2px(40f).toInt()
        )

        layoutParams.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        layoutParams.topMargin = DPIUtil.dp2px(70f).toInt()
//        layoutParams.rightMargin = DPIUtil.dp2px(20f).toInt()
        button.layoutParams = layoutParams
        button.setOnClickListener {
            val alert = AlertDialog.Builder(activity)
                .setItems(serverList.map { it.name }.toTypedArray()) { dialogInterface, i ->
                    if( serverList[i].name == "custom" ) {
                        customDomainDialogt(activity, button)
                    } else {
                        serverSelect(activity, i, button)
                    }

                }.setTitle("서버 선택").create()
            alert.show()
        }
        parent.addView(button)

    }

    fun serverSelect(activity:Activity, index:Int, button:Button) {
        selectedServer = serverList[index]

        button.text = selectedServer.name

        // TODO: 초기화 코드 들어가야 함
        Pref.selectedServer = index

        Toast.makeText(activity, "TODO: 초기화 코드 들어가야 함", Toast.LENGTH_LONG).show()

//                    CommonLogic.onLogOut(WelaaaApplication.context)

//
//                    Pref.set(i, Key.selectedServer.name)
//                    ActivityManager.shared.removeAllBehindActivity(SplashActivity())
    }

    fun customDomainDialogt(activity: Activity, button: Button) {
        val alert = AlertDialog.Builder(activity)
        alert.setTitle("커스텀 서버")

        val layout = LinearLayout(activity)
        layout.orientation = LinearLayout.VERTICAL

        val editApi = EditText(activity)
        editApi.setSingleLine()
        editApi.hint = "api domain"
        editApi.setText(Pref.customApiDomain)
        layout.addView(editApi)

        val editWeb = EditText(activity)
        editWeb.setSingleLine()
        editWeb.hint = "web domain"
        editWeb.setText(Pref.customWebDomain)
        layout.addView(editWeb)

        layout.setPadding(50, 40, 50, 10)

        alert.setView(layout)

        alert.setPositiveButton("Proceed") { _, _ ->

            Pref.customApiDomain = editApi.text.toString()
            Pref.customWebDomain = editWeb.text.toString()

            Toast.makeText(activity, "Saved Sucessfully", Toast.LENGTH_LONG).show()

            serverSelect(activity, Type.custom.ordinal, button)
        }

        alert.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        alert.setCancelable(false)
        alert.show()
    }
}