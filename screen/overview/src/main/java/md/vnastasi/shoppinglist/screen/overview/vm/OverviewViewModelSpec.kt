package md.vnastasi.shoppinglist.screen.overview.vm

import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState

interface OverviewViewModelSpec {

    val viewState: StateFlow<ViewState>

    fun dispatch(event: UiEvent)
}
