package md.vnastasi.shoppinglist.screen.additems.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModelSpec

class StubAddItemsViewModelSpec(private val viewState: ViewState) : AddItemsViewModelSpec {

    override val screenState: StateFlow<ViewState>
        get() = MutableStateFlow(viewState)

    override fun onUiEvent(uiEvent: UiEvent) = Unit
}
