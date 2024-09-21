package md.vnastasi.shoppinglist.screen.additems.vm

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.model.ViewState

interface AddItemsViewModelSpec {

    val screenState: StateFlow<ViewState>

    val searchTermState: MutableState<String>

    fun onUiEvent(uiEvent: UiEvent)
}
