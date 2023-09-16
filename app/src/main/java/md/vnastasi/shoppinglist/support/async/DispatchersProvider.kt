package md.vnastasi.shoppinglist.support.async

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {

    val Main: CoroutineDispatcher

    val MainImediate: CoroutineDispatcher

    val Default: CoroutineDispatcher

    val IO: CoroutineDispatcher
}
