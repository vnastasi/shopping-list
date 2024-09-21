package md.vnastasi.shoppinglist.ui.support

import androidx.test.espresso.idling.concurrent.IdlingThreadPoolExecutor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import md.vnastasi.shoppinglist.support.async.DispatchersProvider
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class UiTestDispatcherProvider : DispatchersProvider {

    private val idlingThreadPoolExecutor = IdlingThreadPoolExecutor(
        /* resourceName = */ "idling-thread-pool",
        /* corePoolSize = */ 4,
        /* maximumPoolSize = */ 8,
        /* keepAliveTime = */ 10,
        /* unit = */ TimeUnit.SECONDS,
        /* workQueue = */ ArrayBlockingQueue(100),
        /* threadFactory = */ Executors.defaultThreadFactory()
    )

    override val Main: CoroutineDispatcher = Dispatchers.Main

    override val MainImmediate: CoroutineDispatcher = Dispatchers.Main.immediate

    override val Default: CoroutineDispatcher = idlingThreadPoolExecutor.asCoroutineDispatcher()

    override val IO: CoroutineDispatcher = idlingThreadPoolExecutor.asCoroutineDispatcher()
}
