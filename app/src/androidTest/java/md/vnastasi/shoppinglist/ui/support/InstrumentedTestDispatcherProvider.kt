package md.vnastasi.shoppinglist.ui.support

import androidx.test.espresso.idling.concurrent.IdlingThreadPoolExecutor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import md.vnastasi.shoppinglist.support.async.DispatchersProvider
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class InstrumentedTestDispatcherProvider : DispatchersProvider {

    private val idlingThreadPoolExecutor = IdlingThreadPoolExecutor(
        "idling-thread-pool",
        4,
        8,
        10,
        TimeUnit.SECONDS,
        ArrayBlockingQueue(100),
        Executors.defaultThreadFactory()
    )

    override val Main: CoroutineDispatcher = Dispatchers.Main

    override val MainImmediate: CoroutineDispatcher = Dispatchers.Main.immediate

    override val Default: CoroutineDispatcher = idlingThreadPoolExecutor.asCoroutineDispatcher()

    override val IO: CoroutineDispatcher = idlingThreadPoolExecutor.asCoroutineDispatcher()
}
