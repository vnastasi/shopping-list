package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModelSpec

class StubAddItemsViewModelSpec(
    viewState: ViewState,
    searchTermValue: String
) : AddItemsViewModelSpec {

    override val viewState: StateFlow<ViewState> = MutableStateFlow(viewState)

    override val searchTermState: MutableState<String> = mutableStateOf(searchTermValue)

    override fun onUiEvent(uiEvent: UiEvent) = Unit
}
