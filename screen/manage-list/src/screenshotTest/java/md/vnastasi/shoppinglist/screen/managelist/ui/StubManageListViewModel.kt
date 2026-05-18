package md.vnastasi.shoppinglist.screen.managelist.ui

import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import md.vnastasi.shoppinglist.screen.managelist.model.Effect
import md.vnastasi.shoppinglist.screen.managelist.model.UiEvent
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState
import md.vnastasi.shoppinglist.screen.managelist.vm.ManageListViewModelSpec

class StubManageListViewModel(
    expectedViewState: ViewState,
    expectedListName: String
) : ManageListViewModelSpec {

    override val listNameTextFieldState: TextFieldState = TextFieldState(expectedListName)

    override val viewState: StateFlow<ViewState> = MutableStateFlow(expectedViewState)

    override val effect: Flow<Effect> = emptyFlow()

    override fun dispatch(event: UiEvent) = Unit
}
