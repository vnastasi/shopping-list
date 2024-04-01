package md.vnastasi.shoppinglist.screen.overview.vm

import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState

interface ListOverviewViewModelSpec {

    val screenState: StateFlow<ViewState>

    fun onUiEvent(uiEvent: UiEvent)
}
