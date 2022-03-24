package xigua.fit.lifecycle

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleRegistry

/**
 * description ： 生命周期用Activity
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/3/22 20:24
 */
open class MyActivity : ComponentActivity() {
    init {
        //通过反射来强制将ComponentActivity中的final LifecycleRegistry变量设置为自定义LifeCycle实现类(然而不行)
        this::class.java.superclass.superclass.declaredFields[4].isAccessible = true
        this::class.java.superclass.superclass.declaredFields[4].set(this,MyLifeCycleRegistry(this) as LifecycleRegistry)
        this::class.java.superclass.superclass.declaredFields[4].isAccessible = false
        ReportFragment.injectIfNeedIn(this)
        Log.d("testTag", lifecycle::class.java.name)
    }


}