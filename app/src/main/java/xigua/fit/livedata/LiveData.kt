package xigua.fit.livedata

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import java.util.*
import kotlin.collections.HashMap

/**
 * description ： 仿写LiveData
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/3/24 21:42
 */
class LiveData<T> {
    private val NOT_SET: T = Any() as T
    private var mVersion = -1
    private var mData: T? = NOT_SET
    private val mObservers = HashMap<Observer<T>, ObserverWrapper>()

    fun observe(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) return
        val observerWrapper = LifecycleBoundObserver(lifecycleOwner, observer)
        val existing = mObservers.mPutIfAbsent(observer, observerWrapper)
        //如果重新为已有observer更改了lifecycleOwner,抛出错误
        if (existing != null && existing.isAttachTo(lifecycleOwner)) {
            throw IllegalArgumentException(
                "Cannot add the same observer"
                        + " with different lifecycles"
            )
        }
        //实现根据生命周期管理
        lifecycleOwner.lifecycle.addObserver(observerWrapper)
    }

    fun observeForever(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    }

    fun setValue(value: T) {}

    fun postValue(value: T) {}

    fun dispatchValue(value: T?) {}

    fun considerNotify(observer: Observer<T>) {}

    /**
     * description ： Observer包装类
     * author : Watermelon02
     * email : 1446157077@qq.com
     * date : 2022/3/24 21:59
     */
    abstract inner class ObserverWrapper(
        val mLifecycleOwner: LifecycleOwner,
        private val observer: Observer<T>
    ):LifecycleObserver {
        var mLastVersion = 0
        abstract fun detachObserver()
        abstract fun shouldBeActive(): Boolean
        abstract fun isAttachTo(lifecycleOwner: LifecycleOwner): Boolean
        fun isActive() = mLifecycleOwner.lifecycle.currentState > Lifecycle.State.DESTROYED

    }

    /**
     * description ： observe()方法所需的包装类
     * author : Watermelon02
     * email : 1446157077@qq.com
     * date : 2022/3/24 21:42
     */
    inner class LifecycleBoundObserver(mLifecycleOwner: LifecycleOwner, observer: Observer<T>) :
        ObserverWrapper(mLifecycleOwner, observer) {
        init {

        }

        override fun detachObserver() {
            TODO("Not yet implemented")
        }

        override fun shouldBeActive(): Boolean {
            TODO("Not yet implemented")
        }

        override fun isAttachTo(lifecycleOwner: LifecycleOwner): Boolean {
            return mLifecycleOwner == lifecycleOwner
        }
    }

    /**
     * description ： observeForever()所需的包装类
     * author : Watermelon02
     * email : 1446157077@qq.com
     * date : 2022/3/24 21:42
     */
    inner class LifecycleAlwaysObserver() {

    }

    fun HashMap<Observer<T>, ObserverWrapper>.mPutIfAbsent(
        observer: Observer<T>,
        observerWrapper: ObserverWrapper
    ): ObserverWrapper? = if (contains(observer)) get(observer) else {//扩展HashMap仿源码中的putIfAbsent方法
        put(observer, observerWrapper)
        null
    }
}
