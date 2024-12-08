package md.vnastasi.shoppinglist.screen.listdetails.vm

import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState

interface ListDetailsViewModelSpec {

    val viewState: StateFlow<ViewState>

    fun onUiEvent(event: UiEvent)
}
