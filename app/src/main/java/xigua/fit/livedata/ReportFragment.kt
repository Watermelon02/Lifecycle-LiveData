package xigua.fit.lifecycle

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * description ： 感知生命周期用Fragment
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/3/22 20:51
 */
class ReportFragment : android.app.Fragment() {
    companion object {
        private val REPORT_FRAGMENT_TAG = ("androidx.lifecycle"
                + ".LifecycleDispatcher.report_fragment_tag")

        fun injectIfNeedIn(activity: Activity) {//注入Fragment以感知
            val fragment = ReportFragment()
            //getFragmentManager()已经被弃用，但是getSupportFragmentManager是在FragmentActivity中实现的，所以暂不考虑
            activity.fragmentManager.beginTransaction().add(fragment, REPORT_FRAGMENT_TAG).commit()
            activity.fragmentManager.executePendingTransactions()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dispatch(Lifecycle.Event.ON_CREATE)
    }

    override fun onStart() {
        super.onStart()
        dispatch(Lifecycle.Event.ON_START)
    }

    override fun onResume() {
        super.onResume()
        dispatch(Lifecycle.Event.ON_RESUME)
    }

    override fun onPause() {
        super.onPause()
        dispatch(Lifecycle.Event.ON_PAUSE)
    }

    override fun onStop() {
        super.onStop()
        dispatch(Lifecycle.Event.ON_STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        dispatch(Lifecycle.Event.ON_DESTROY)
    }

    private fun dispatch(event: Lifecycle.Event) {//源码中还要判断类型，但是此处图简单直接强转类型
        (activity as LifecycleOwner).also { (it.lifecycle as MyLifeCycleRegistry).handleLifecycleEvent(event) }

    }
}