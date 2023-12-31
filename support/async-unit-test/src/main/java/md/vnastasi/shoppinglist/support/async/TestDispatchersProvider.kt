package md.vnastasi.shoppinglist.support.async

import kotlinx.coroutines.CoroutineDispatcher

class TestDispatchersProvider(private val dispatcher: CoroutineDispatcher) : DispatchersProvider {

    override val Main: CoroutineDispatcher
        get() = dispatcher

    override val MainImmediate: CoroutineDispatcher
        get() = dispatcher

    override val Default: CoroutineDispatcher
        get() = dispatcher

    override val IO: CoroutineDispatcher
        get() = dispatcher
}
