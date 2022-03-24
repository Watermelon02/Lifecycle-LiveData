package xigua.fit.lifecycle

import androidx.lifecycle.*
import java.lang.ref.WeakReference

/**
 * description ： 自定义LifeCycle实现类
 * author : Watermelon02
 * email : 1446157077@qq.com
 * date : 2022/3/22 22:14
 */
class MyLifeCycleRegistry(val provider: LifecycleOwner) :
    LifecycleRegistry(provider) {

    //使用WeakReference,避免直接持有Activity,防止内存泄漏
    private val lifecycleOwner: WeakReference<LifecycleOwner?>? = WeakReference(provider)
    private val mObservers = HashMap<LifecycleObserver, ObserverWithStata>()

    override fun addObserver(observer: LifecycleObserver) {
        val newObserverWithState = ObserverWithStata(observer, State.INITIALIZED)
        val pre = mObservers[observer]
        //如果已经有该Fragment,则退出
        if (pre != null) return
        mObservers.put(observer, newObserverWithState)
        if (lifecycleOwner == null) return
        //为新增的observer回调到当前state
        while (newObserverWithState.state < currentState && mObservers.contains(observer)) {
            val event = Event.upFrom(newObserverWithState.state)
            event?.let {
                newObserverWithState.dispatchEvent(event)
                newObserverWithState.state.ordinal + 1
            }
        }
    }

    override fun removeObserver(observer: LifecycleObserver) {
        mObservers.remove(observer)
    }

    override fun getCurrentState(): State {
        return currentState
    }

    override fun handleLifecycleEvent(event: Event){
        for (observer in mObservers){
            observer.value.dispatchEvent(event)
        }
        currentState = event.targetState
    }

    inner class ObserverWithStata(
        private val lifecycleObserver: LifecycleObserver,
        val state: State
    ) {
        private lateinit var lifecycleEventObserver: LifecycleEventObserver

        //源码中在这里将observer封装为ObserverWithState,该类用于将不同Observer用LifeCycling进行封装，此处省略
        init {
            when (lifecycleObserver) {
                is LifecycleEventObserver -> lifecycleEventObserver = lifecycleObserver
            }
        }

        fun dispatchEvent(event: Event) {
            lifecycleOwner?.get()?.let {
                lifecycleEventObserver.onStateChanged(it, event)
            }
        }
    }

}