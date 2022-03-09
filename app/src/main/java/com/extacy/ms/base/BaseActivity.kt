package com.extacy.ms.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.hold1.keyboardheightprovider.KeyboardHeightProvider
import com.extacy.ms.BuildConfig
import com.extacy.ms.R
import com.extacy.ms.net.UrlSelector
import com.extacy.ms.common.utils.DPIUtil
import com.extacy.ms.components.dialog.*
import com.extacy.ms.components.toast.ToastView
import com.extacy.ms.net.core.BaseRequestable


fun FragmentManager.clearBackStack() {
    popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

interface KeyboardHeightListener {
    fun onKeyboardHeight(height: Int)
}
open class BaseActivity : AppCompatActivity() {
    // TODO: 종료 문구 처리 필요
    var doubleBackToExitPressedOnce = false
    private var isBacking = false

    private var keyboardHeightProvider: KeyboardHeightProvider? = null

    private var keyboardHeightListeners: ArrayList<KeyboardHeightListener> = ArrayList()
    var requestable = object: BaseRequestable() {
        override fun context(): Context? {
            return this@BaseActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DPIUtil.updateMatrics(this)
        addServerSelectView()
        keyboardHeightProvider = KeyboardHeightProvider(this)
        keyboardHeightProvider?.addKeyboardListener(getKeyboardListener())
    }

    private fun getKeyboardListener() = object : KeyboardHeightProvider.KeyboardListener {
        override fun onHeightChanged(height: Int) {
            keyboardHeightListeners.forEach { listener ->
                listener.onKeyboardHeight(height)
            }
        }
    }

    fun registKeyboardHeightListener(listener: KeyboardHeightListener) {
        if(!keyboardHeightListeners.contains(listener)) {
            keyboardHeightListeners.add(listener)
        }
    }

    fun unregistKeyboardHeightListener(listener: KeyboardHeightListener) {
        if( keyboardHeightListeners.contains(listener) ) {
            keyboardHeightListeners.remove(listener)
        }
    }


    private fun addServerSelectView() {
        if( BuildConfig.DEBUG ) {
            UrlSelector.serverSelectViewSetting(this, window.decorView.rootView as FrameLayout)
        }
    }
    override fun onResume() {
        super.onResume()
        hideKeyBoard(this)
        keyboardHeightProvider?.onResume()
    }

    override fun onPause() {
        super.onPause()
        keyboardHeightProvider?.onPause()
    }
    @SuppressLint("ClickableViewAccessibility")
    fun setTouchHideKeyboard(view: View) {
        view.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyBoard(this)
            }
            return@setOnTouchListener false
        }
    }

    fun hideKeyBoard(activity: Activity) {
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        val finalView: View = view
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(finalView.applicationWindowToken, 0)
        view.clearFocus()
    }

    fun showKeyboard(ed: EditText) {
        val imm =
            application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        ed.postDelayed(
            {
                ed.requestFocus()
                imm.showSoftInput(ed, 0)
            }, 800
        )
    }

    // present
    fun pushFragment(fragment: Fragment, container: Int = R.id.fragment_container) {
        addFragment(fragment, Transition.Push, container)
    }

    fun modelFragment(fragment: Fragment, container: Int = R.id.fragment_container) {
        addFragment(fragment, Transition.Modal, container)
    }

    fun addFragment(fragment: Fragment, transition: Transition = Transition.Push, container: Int = R.id.fragment_container) {
        if( fragment is BaseFragment){
            fragment.transition = transition
        }
        val show = transition.show()
        val dismiss = transition.dismiss()
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(show.inAnim, show.outAnim, dismiss.inAnim, dismiss.outAnim)
            .add(container, fragment, fragment.javaClass.name)
            .addToBackStack(fragment.javaClass.name)
            .commitAllowingStateLoss()
    }

    // dismiss
    fun popBackStack() {
//        var transition = Transition.Push
//        if( fragment is BaseFragment ) {
//            transition = fragment.transition
//        }
//
//        val anim = transition.dismiss()
//        supportFragmentManager
//            .beginTransaction()
//            .setCustomAnimations(anim.inAnim, anim.outAnim)
//            .remove(fragment)
//            .commitAllowingStateLoss()

        supportFragmentManager.popBackStack()
    }

    // replace
    fun replaceFragment(fragment: Fragment, container: Int = R.id.fragment_container, anim: TransitionAnim = Transition.Replace.show()) {
        if( supportFragmentManager.backStackEntryCount > 0 ) {
            supportFragmentManager.clearBackStack()
        }

        val transaction = supportFragmentManager.beginTransaction()


        if( anim != Transition.None.show() ){
            transaction.setCustomAnimations(anim.inAnim, anim.outAnim)
        }

        transaction.replace(container, fragment)
            .commitAllowingStateLoss()
    }

    fun replaceNextFragment(fragment: Fragment, container: Int = R.id.fragment_container) {
        replaceFragment(fragment, container, Transition.ReplaceNext.show())
    }

    fun replacePrevFragment(fragment: Fragment, container: Int = R.id.fragment_container) {
        replaceFragment(fragment, container, Transition.ReplacePrev.show())
    }


//    override fun onBackPressed() {
//        supportFragmentManager.backStack
//        if (fragmentList.size > 0) {
//            if (fragmentList[fragmentList.size - 1].onBackPressed() === false) {
//                return
//            }
//            if (isBacking) {
//                return
//            }
//            if (fragmentList.size == 1) {
//                finish()
//                return
//            }
//
//            fragmentList[fragmentList.size - 1].popFragment()
//            if (!isBacking) {
//                isBacking = true
//                Delay.main(500) {
//                    isBacking = false
//                }
//            }
//            return
//        }
//
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed()
//            return
//        }
//        doubleBackToExitPressedOnce = true
//        Delay.main(1500) {
//            doubleBackToExitPressedOnce = false
//        }
//    }

    // push
    // replace
    // remove

    // stack

    fun showAlert(
        content: String,
        confirmTitle: String? = null,
        confirmCallback: ButtonCallback? = null,
    ) {
        showAlert("알림", content, confirmTitle, confirmCallback)
    }
    fun showAlert(
        title: String? = null,
        content: String,
        confirmTitle: String? = null,
        confirmCallback: ButtonCallback? = null,
    ) {
        showConfirm(title, content, confirmTitle, confirmCallback)
    }

    fun showConfirm(
        content: String,
        confirmTitle: String? = null,
        confirmCallback: ButtonCallback? = null,
        cancelTitle: String? = null,
        cancelCallback: ButtonCallback? = null,
    ) {
        showConfirm("알림", content, confirmTitle, confirmCallback, cancelTitle, cancelCallback)
    }
    fun showConfirm(
        title: String? = null,
        content: String,
        confirmTitle: String? = null,
        confirmCallback: ButtonCallback? = null,
        cancelTitle: String? = null,
        cancelCallback: ButtonCallback? = null,
    ) {

        val combine = PopupCombin(this)

        val titleView = PopupTitle(this)
        titleView.setting(title)

        val contentView = PopupTextContent(this)
        contentView.setting(content)

        val buttonView = PopupButton(this)
        buttonView.setting(combine, confirmTitle, confirmCallback, cancelTitle, cancelCallback)

        combine.show(arrayListOf(titleView, contentView, buttonView))
    }

    fun showToast(message: String) {
        ToastView.show(this, message)
    }
}