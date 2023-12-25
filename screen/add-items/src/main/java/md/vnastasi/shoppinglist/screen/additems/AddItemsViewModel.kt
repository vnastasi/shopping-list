package md.vnastasi.shoppinglist.screen.additems

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.repository.NameSuggestionRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.support.async.DispatchersProvider
import md.vnastasi.shoppinglist.support.ui.toast.ToastMessage

class AddItemsViewModel internal constructor(
    savedStateHandle: SavedStateHandle,
    private val nameSuggestionRepository: NameSuggestionRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val shoppingItemRepository: ShoppingItemRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _screenState = MutableStateFlow(ViewState.init())
    val screenState: StateFlow<ViewState> = _screenState.asStateFlow()

    private val shoppingList = savedStateHandle.getStateFlow(ARG_KEY_SHOPPING_LIST_ID, Long.MIN_VALUE)
        .filter { it != Long.MIN_VALUE }
        .flatMapConcat { shoppingListRepository.findById(it) }

    fun onUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.SearchTermChanged -> onSearchTermChanged(uiEvent.newSearchTerm)
            is UiEvent.ItemAddedToList -> onItemAddedToList(uiEvent.name)
            is UiEvent.SuggestionDeleted -> onSuggestionDeleted(uiEvent.suggestion)
            is UiEvent.ToastShown -> onToastShown()
        }
    }

    private fun onSearchTermChanged(newSearchTerm: String) {
        viewModelScope.launch(dispatchersProvider.Main) {
            nameSuggestionRepository.findAllMatching(newSearchTerm).collectLatest { suggestions ->
                _screenState.update { viewState ->
                    viewState.copy(searchTerm = newSearchTerm, suggestions = suggestions.toImmutableList())
                }
            }
        }
    }

    private fun onItemAddedToList(name: String) {
        viewModelScope.launch(dispatchersProvider.Main) {
            shoppingList
                .map { ShoppingItem(name = name, isChecked = false, list = it) }
                .collectLatest { shoppingItem ->
                    shoppingItemRepository.create(shoppingItem)
                    nameSuggestionRepository.create(shoppingItem.name)
                    _screenState.update { viewState ->
                        val toastMessage = ToastMessage(textResourceId = R.string.toast_item_added, arguments = persistentListOf(name))
                        viewState.copy(toastMessage = toastMessage)
                    }
                }
        }
    }

    private fun onSuggestionDeleted(suggestion: NameSuggestion) {
        viewModelScope.launch(dispatchersProvider.Main) {
            nameSuggestionRepository.delete(suggestion)
            _screenState.update { viewState ->
                val toastMessage = ToastMessage(textResourceId = R.string.toast_suggestion_removed, arguments = persistentListOf(suggestion.name))
                viewState.copy(toastMessage = toastMessage)
            }
        }
    }

    private fun onToastShown() {
        viewModelScope.launch(dispatchersProvider.Main) {
            _screenState.update { viewState ->
                viewState.copy(toastMessage = null)
            }
        }
    }

    class Factory internal constructor(
        private val nameSuggestionRepository: NameSuggestionRepository,
        private val shoppingListRepository: ShoppingListRepository,
        private val shoppingItemRepository: ShoppingItemRepository,
        private val dispatchersProvider: DispatchersProvider
    ) : ViewModelProvider.Factory by viewModelFactory({
        initializer {
            AddItemsViewModel(createSavedStateHandle(), nameSuggestionRepository, shoppingListRepository, shoppingItemRepository, dispatchersProvider)
        }
    })

    companion object {

        const val ARG_KEY_SHOPPING_LIST_ID = "SHOPPING_LIST_ID"
    }
}
