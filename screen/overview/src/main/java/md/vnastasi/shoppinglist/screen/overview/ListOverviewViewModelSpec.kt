package md.vnastasi.shoppinglist.screen.overview

import kotlinx.coroutines.flow.StateFlow

interface ListOverviewViewModelSpec {

    val screenState: StateFlow<ViewState>

    fun onUiEvent(uiEvent: UiEvent)
}
