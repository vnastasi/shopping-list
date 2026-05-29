package md.vnastasi.shoppinglist.screen.listdetails.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import md.vnastasi.shoppinglist.screen.listdetails.model.Effect
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModelSpec

internal class StubListDetailsViewModelSpec(stubViewState: ViewState) : ListDetailsViewModelSpec {

    override val viewState: StateFlow<ViewState> = MutableStateFlow(stubViewState)

    override val effect: Flow<Effect> = emptyFlow()

    override fun dispatch(event: UiEvent) = Unit
}
