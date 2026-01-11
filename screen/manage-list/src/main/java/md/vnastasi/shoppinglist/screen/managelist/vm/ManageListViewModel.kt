package md.vnastasi.shoppinglist.screen.managelist.vm

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.managelist.model.TextValidationError
import md.vnastasi.shoppinglist.screen.managelist.model.UiEvent
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.coroutine.FLOW_SUBSCRIPTION_TIMEOUT

@HiltViewModel(assistedFactory = ManageListViewModel.Factory::class)
class ManageListViewModel @AssistedInject constructor(
    @Assisted private val shoppingListId: Long?,
    private val repository: ShoppingListRepository,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), ManageListViewModelSpec {

    private val _shoppingListName = MutableStateFlow("")
    private val _validationError = MutableStateFlow(TextValidationError.NONE)
    private val _isSaveEnabled = MutableStateFlow(false)



    override val listNameTextFieldState: TextFieldState = TextFieldState()

    override val viewState: StateFlow<ViewState> = combine(
        flow = _validationError,
        flow2 = _isSaveEnabled,
        transform = ::ViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT),
        initialValue = ViewState.INIT
    )

    init {
        if (shoppingListId != null) {
            viewModelScope.launch {
                repository.findById(shoppingListId).collect { shoppingList -> listNameTextFieldState.setTextAndPlaceCursorAtEnd(shoppingList.name) }
            }
        }
    }

    override fun dispatch(event: UiEvent) {
        when (event) {
            is UiEvent.OnNameChange -> handleNameChange(event.name)
            is UiEvent.OnSaveList -> handleListSave(event.name)
        }
    }

    private fun handleNameChange(name: String) {
        _shoppingListName.update { name }

        val textValidationError = when {
            name.isEmpty() -> TextValidationError.EMPTY
            name.isBlank() -> TextValidationError.BLANK
            else -> TextValidationError.NONE
        }
        _validationError.update { textValidationError }

        _isSaveEnabled.update { textValidationError == TextValidationError.NONE }
    }

    private fun handleListSave(name: String) {
        viewModelScope.launch {
            if (shoppingListId != null) {
                val shoppingList = repository.findById(shoppingListId).first()
                repository.update(shoppingList.copy(name = name))
            } else {
                repository.create(ShoppingList(name = name))
            }
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(listId: Long?): ManageListViewModel
    }
}
