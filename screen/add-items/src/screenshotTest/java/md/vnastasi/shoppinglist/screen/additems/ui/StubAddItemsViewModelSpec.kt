package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import md.vnastasi.shoppinglist.screen.additems.model.Effect
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModelSpec

class StubAddItemsViewModelSpec(
    viewState: ViewState,
    searchTermValue: String
) : AddItemsViewModelSpec {

    override val searchTermTextFieldState: TextFieldState = TextFieldState(searchTermValue)

    override val viewState: StateFlow<ViewState> = MutableStateFlow(viewState)

    override val effect: Flow<Effect> = emptyFlow()

    override fun dispatch(uiEvent: UiEvent) = Unit
}
