package md.vnastasi.shoppinglist.screen.additems.vm

import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.additems.model.Effect
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.model.ViewState

internal interface AddItemsViewModelSpec {

    val searchTermTextFieldState: TextFieldState

    val viewState: StateFlow<ViewState>

    val effect: Flow<Effect>

    fun dispatch(uiEvent: UiEvent)
}
