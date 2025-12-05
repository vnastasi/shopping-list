package md.vnastasi.shoppinglist.screen.overview.vm

import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState

interface ManageListViewModelSpec {

    val listNameTextFieldState : TextFieldState

    val viewState: StateFlow<ViewState>

    fun dispatch(event: UiEvent)
}
