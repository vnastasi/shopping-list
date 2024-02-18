package md.vnastasi.shoppinglist.support.async

import kotlinx.coroutines.CoroutineDispatcher

@Suppress("PropertyName", "VariableNaming")
interface DispatchersProvider {

    val Main: CoroutineDispatcher

    val MainImmediate: CoroutineDispatcher

    val Default: CoroutineDispatcher

    val IO: CoroutineDispatcher
}
