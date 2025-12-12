package md.vnastasi.shoppinglist.screen.managelist.vm

import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.managelist.model.UiEvent
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState

interface ManageListViewModelSpec {

    val viewState: StateFlow<ViewState>

    fun dispatch(event: UiEvent)
}
