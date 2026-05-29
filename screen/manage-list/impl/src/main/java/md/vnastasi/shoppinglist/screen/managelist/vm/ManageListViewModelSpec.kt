package md.vnastasi.shoppinglist.screen.managelist.vm

import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.managelist.model.Effect
import md.vnastasi.shoppinglist.screen.managelist.model.UiEvent
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState

interface ManageListViewModelSpec {

    val listNameTextFieldState: TextFieldState

    val viewState: StateFlow<ViewState>

    val effect: Flow<Effect>

    fun dispatch(event: UiEvent)
}
