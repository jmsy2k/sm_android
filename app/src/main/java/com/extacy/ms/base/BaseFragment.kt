package com.extacy.ms.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.extacy.ms.R
import com.extacy.ms.components.dialog.ButtonCallback
import com.extacy.ms.net.core.BaseRequestable

open class BaseFragment: Fragment() {
    var transition: Transition = Transition.None

    var requestable = object: BaseRequestable() {
        override fun context(): Context? {
            return requireContext()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isClickable = true
    }
    override fun onDestroyView() {
        super.onDestroyView()
        clearRequests()
    }

    val baseActivity: BaseActivity
        get() = requireActivity() as BaseActivity


    fun clearRequests() {
        requestable.requesters.forEach { it.cancel() }
        requestable.requesters.clear()
    }

    fun pushFragment(fragment: Fragment, container: Int = R.id.fragment_container) {
        baseActivity.pushFragment(fragment, container)
    }

    fun modalFragment(fragment: Fragment, container: Int = R.id.fragment_container) {
        baseActivity.modelFragment(fragment, container)
    }

    fun popBackStack() {
        baseActivity.popBackStack()
    }

    fun replaceFragment(fragment: Fragment, container: Int = R.id.fragment_container) {
        baseActivity.replaceFragment(fragment, container)
    }

    fun replaceNextFragment(fragment: Fragment, container: Int = R.id.fragment_container) {
        baseActivity.replaceNextFragment(fragment, container)
    }

    fun replacePrevFragment(fragment: Fragment, container: Int = R.id.fragment_container) {
        baseActivity.replacePrevFragment(fragment, container)
    }

//    open fun onBackPressed(): Boolean {
//        return true
//    }

//    open fun popFragment() {
//        if (activity is BaseActivity) {
//            if (headerView != null && HeaderView.CLOSE_STYLE === headerView?.buttonStyle) {
//                (activity as BaseActivity).popBackStackClose(this, true)
//                return
//            }
//            (activity as BaseActivity).popBackStack(this, true)
//        }
//    }

//    private fun contentInit(view: View) {
//        DPIUtil.updateMatrics(requireActivity())
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val decor: View? = activity?.window?.decorView
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                decor?.windowInsetsController?.setSystemBarsAppearance(
//                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
//                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//                )
//            } else {
//                decor?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            }
//        }
//
//
//
//        activity?.let { hideKeyBoard(it) }
//        motherView = view
//        motherView?.setOnClickListener(View.OnClickListener { })
//
//        setup()
//    }
//
//    abstract fun setup()
//
//    protected fun setStatubarColor(color: Int) {
//        statusBarHexColor = color
//        (activity as WelaaaBaseActivity).setFragmentStatusBarHexColor(color)
//    }
//
//    fun setStatusbarNoAnimation(hex: Int) {
//        statusBarHexColor = hex
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            (activity as WelaaaBaseActivity).window.statusBarColor = statusBarHexColor
//        }
//    }
//
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (activity is WelaaaBaseActivity) {
//            (activity as WelaaaBaseActivity).fragmentList.remove(this)
//        }
//    }
//
//    fun replaceFragment(fragment: Fragment?) {
//        if (activity is WelaaaBaseActivity) {
//            motherView?.isClickable = false
//            if (fragment != null) {
//                (activity as WelaaaBaseActivity).replaceFragment(fragment)
//            }
//        }
//    }
//
//    fun noAnimationFragment(fragment: Fragment?) {
//        if (activity is WelaaaBaseActivity) {
//            motherView?.isClickable = false
//            if (fragment != null) {
//                (activity as WelaaaBaseActivity).noAnimationFragment(fragment)
//            }
//        }
//    }
//
//    fun pushFragment(fragment: Fragment?) {
//        if (activity is WelaaaBaseActivity) {
//            motherView?.isClickable = false
//            if (fragment != null) {
//                (activity as WelaaaBaseActivity).pushFragment(fragment)
//            }
//        }
//    }
//
//    fun pushUpFragment(fragment: Fragment?) {
//        if (activity is WelaaaBaseActivity) {
//            motherView?.isClickable = false
//            if (fragment != null) {
//                (activity as WelaaaBaseActivity).pushUpFragment(fragment)
//            }
//        }
//    }

//    fun removeCount(count: Int) {
//        if (activity is WelaaaBaseActivity) {
//            (activity as WelaaaBaseActivity).removeCount(count)
//        }
//    }
//
//    open fun popFragment() {
//        if (activity is WelaaaBaseActivity) {
//            if (headerView != null && HeaderView.CLOSE_STYLE === headerView?.buttonStyle) {
//                (activity as WelaaaBaseActivity).popBackStackClose(this, true)
//                return
//            }
//            (activity as WelaaaBaseActivity).popBackStack(this, true)
//        }
//    }
//
//    fun popFragment(fragment: WelaaaBaseFragment) {
//        // no animation
//        if (activity is WelaaaBaseActivity) {
//            if (fragment.headerView != null && HeaderView.CLOSE_STYLE === fragment.headerView?.buttonStyle) {
//                (activity as WelaaaBaseActivity).popBackStackClose(this, false)
//                return
//            }
//            (activity as WelaaaBaseActivity).popBackStack(fragment, false)
//        }
//    }

    fun registKeyboardHeightListener(listener: KeyboardHeightListener) {
        baseActivity.registKeyboardHeightListener(listener)
    }

    fun unregistKeyboardHeightListener(listener: KeyboardHeightListener) {
        baseActivity.unregistKeyboardHeightListener(listener)
    }
    fun setTouchHideKeyboard(view: View) {
        baseActivity.setTouchHideKeyboard(view)
    }

    fun hideKeyBoard(activity: Activity) {
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }

        (activity as BaseActivity).hideKeyBoard(activity)
    }

    fun showKeyboard(ed: EditText) {
        (activity as BaseActivity).showKeyboard(ed)
    }


    fun showAlert(content: String,
                  confirmTitle:String? = null,
                  confirmCallback: ButtonCallback? = null) {
        baseActivity.showAlert(content, confirmTitle, confirmCallback)
    }
    fun showAlert(title: String? = null,
                  content: String,
                  confirmTitle:String? = null,
                  confirmCallback: ButtonCallback? = null) {
        baseActivity.showAlert(title, content, confirmTitle, confirmCallback)
    }

    fun showConfirm(content: String,
                    confirmTitle: String? = null,
                    confirmCallback: ButtonCallback? = null,
                    cancelTitle: String? = null,
                    cancelCallback: ButtonCallback? = null) {
        baseActivity.showConfirm(content, confirmTitle, confirmCallback, cancelTitle, cancelCallback)
    }

    fun showConfirm(title: String? = null,
                    content: String,
                    confirmTitle: String? = null,
                    confirmCallback: ButtonCallback? = null,
                    cancelTitle: String? = null,
                    cancelCallback: ButtonCallback? = null) {
        baseActivity.showConfirm(title, content, confirmTitle, confirmCallback, cancelTitle, cancelCallback)
    }

    fun showToast(message: String) {
        baseActivity.showToast(message)
    }
}