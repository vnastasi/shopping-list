package md.vnastasi.shoppinglist.screen.listdetails.vm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.listdetails.model.Effect
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState

internal interface ListDetailsViewModelSpec {

    val viewState: StateFlow<ViewState>

    val effect: Flow<Effect>

    fun dispatch(event: UiEvent)
}
