package md.vnastasi.shoppinglist.screen.managelist.ui

import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.managelist.model.UiEvent
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState
import md.vnastasi.shoppinglist.screen.managelist.vm.ManageListViewModelSpec

class StubManageListViewModel(
    private val expectedViewState: ViewState,
    expectedListName: String
) : ManageListViewModelSpec {

    override val listNameTextFieldState: TextFieldState = TextFieldState(expectedListName)

    override val viewState: StateFlow<ViewState>
        get() = MutableStateFlow(expectedViewState)

    override fun dispatch(event: UiEvent) = Unit
}
