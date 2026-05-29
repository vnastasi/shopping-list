package md.vnastasi.shoppinglist.screen.overview.vm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.overview.model.Effect
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState

internal interface OverviewViewModelSpec {

    val viewState: StateFlow<ViewState>

    val effect: Flow<Effect>

    fun dispatch(event: UiEvent)
}
