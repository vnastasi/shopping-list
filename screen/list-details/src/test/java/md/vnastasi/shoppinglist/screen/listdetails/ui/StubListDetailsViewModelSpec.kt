package md.vnastasi.shoppinglist.screen.listdetails.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModelSpec

class StubListDetailsViewModelSpec(private val viewState: ViewState) : ListDetailsViewModelSpec {

    override val screenState: StateFlow<ViewState>
        get() = MutableStateFlow(viewState)

    override fun onUiEvent(event: UiEvent) = Unit
}
