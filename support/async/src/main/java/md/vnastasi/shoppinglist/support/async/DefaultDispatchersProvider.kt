package md.vnastasi.shoppinglist.support.async

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class DefaultDispatchersProvider : DispatchersProvider {

    override val Main: CoroutineDispatcher = Dispatchers.Main

    override val MainImmediate: CoroutineDispatcher = Dispatchers.Main.immediate

    override val Default: CoroutineDispatcher = Dispatchers.Default

    override val IO: CoroutineDispatcher = Dispatchers.IO
}
