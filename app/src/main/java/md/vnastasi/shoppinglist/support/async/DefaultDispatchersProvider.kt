package md.vnastasi.shoppinglist.support.async

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultDispatchersProvider : DispatchersProvider {

    override val Main: CoroutineDispatcher = Dispatchers.Main

    override val MainImediate: CoroutineDispatcher = Dispatchers.Main.immediate

    override val Default: CoroutineDispatcher = Dispatchers.Default

    override val IO: CoroutineDispatcher = Dispatchers.IO
}
