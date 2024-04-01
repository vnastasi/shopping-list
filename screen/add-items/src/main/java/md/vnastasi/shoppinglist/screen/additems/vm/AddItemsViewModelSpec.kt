package md.vnastasi.shoppinglist.screen.additems.vm

import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.model.ViewState

interface AddItemsViewModelSpec {

    val screenState: StateFlow<ViewState>

    fun onUiEvent(uiEvent: UiEvent)
}
