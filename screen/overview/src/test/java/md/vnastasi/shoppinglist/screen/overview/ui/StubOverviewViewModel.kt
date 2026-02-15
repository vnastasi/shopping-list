package md.vnastasi.shoppinglist.screen.overview.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.overview.vm.OverviewViewModelSpec

class StubOverviewViewModel(stubViewState: ViewState) : OverviewViewModelSpec {

    override val viewState: StateFlow<ViewState> = MutableStateFlow(stubViewState)

    override fun dispatch(event: UiEvent) = Unit
}
