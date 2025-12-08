package md.vnastasi.shoppinglist.screen.managelist.vm

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.managelist.model.TextValidationError
import md.vnastasi.shoppinglist.screen.managelist.model.UiEvent
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState

@HiltViewModel(assistedFactory = ManageListViewModel.Factory::class)
class ManageListViewModel @AssistedInject constructor(
    @Assisted private val shoppingListId: Long?,
    private val repository: ShoppingListRepository,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), ManageListViewModelSpec {

    init {
        if (shoppingListId != null) {
            viewModelScope.launch {
                repository.findById(shoppingListId).collect { listNameTextFieldState.setTextAndPlaceCursorAtEnd(it.name) }
            }
        }
    }

    override val listNameTextFieldState: TextFieldState = TextFieldState()

    override val viewState: StateFlow<ViewState> = snapshotFlow { listNameTextFieldState.text.toString() }
        .map { createViewState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ViewState(textValidationError = TextValidationError.NONE, isSaveEnabled = false)
        )

    override fun dispatch(event: UiEvent) {
        when (event) {
            is UiEvent.Saved -> saveList()
        }
    }

    private fun saveList() {
        viewModelScope.launch {
            val newListName = listNameTextFieldState.text.toString()
            if (shoppingListId != null) {
                val shoppingList = repository.findById(shoppingListId).first()
                repository.update(shoppingList.copy(name = newListName))
            } else {
                repository.create(ShoppingList(name = newListName))
            }
        }
    }

    private fun createViewState(): ViewState {
        val name = listNameTextFieldState.text.toString()
        val textValidationError = when {
            name.isEmpty() -> TextValidationError.EMPTY
            name.isBlank() -> TextValidationError.BLANK
            else -> TextValidationError.NONE
        }
        return ViewState(
            textValidationError = textValidationError,
            isSaveEnabled = textValidationError == TextValidationError.NONE
        )
    }

    @AssistedFactory
    interface Factory {

        fun create(listId: Long?): ManageListViewModel
    }
}