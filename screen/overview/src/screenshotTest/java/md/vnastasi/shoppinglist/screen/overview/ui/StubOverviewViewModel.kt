package md.vnastasi.shoppinglist.screen.overview.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import md.vnastasi.shoppinglist.screen.overview.model.Effect
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.overview.vm.OverviewViewModelSpec

internal class StubOverviewViewModel(stubViewState: ViewState) : OverviewViewModelSpec {

    override val viewState: StateFlow<ViewState> = MutableStateFlow(stubViewState)

    override val effect: Flow<Effect> = emptyFlow()

    override fun dispatch(event: UiEvent) = Unit
}
