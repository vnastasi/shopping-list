package md.vnastasi.shoppinglist.screen.listdetails.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModelSpec

class StubListDetailsViewModelSpec(stubViewState: ViewState) : ListDetailsViewModelSpec {

    override val viewState: StateFlow<ViewState> = MutableStateFlow(stubViewState)

    override fun dispatch(event: UiEvent) = Unit
}
