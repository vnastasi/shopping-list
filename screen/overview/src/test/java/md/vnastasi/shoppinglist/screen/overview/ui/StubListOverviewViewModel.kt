package md.vnastasi.shoppinglist.screen.overview.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.overview.vm.ListOverviewViewModelSpec

class StubListOverviewViewModel(private val viewState: ViewState) : ListOverviewViewModelSpec {

    override val screenState: StateFlow<ViewState>
        get() = MutableStateFlow(viewState)

    override fun onUiEvent(uiEvent: UiEvent) = Unit
}
