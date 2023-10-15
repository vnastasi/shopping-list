package md.vnastasi.shoppinglist.screen.additems

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.repository.NameSuggestionRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.support.async.DispatchersProvider

class AddItemsViewModel(
    savedStateHandle: SavedStateHandle,
    private val nameSuggestionRepository: NameSuggestionRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val shoppingItemRepository: ShoppingItemRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private val _screenState = MutableStateFlow<ViewState>(ViewState.init())
    val screenState: StateFlow<ViewState> = _screenState.asStateFlow()

    private val shoppingList = savedStateHandle.getStateFlow(ARG_KEY_SHOPPING_LIST_ID, Long.MIN_VALUE).filter { it != Long.MIN_VALUE }.flatMapConcat { shoppingListRepository.findById(it) }

    fun onUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.OnSearchTermChanged -> onSearchTermChanged(uiEvent.newSearchTerm)
            is UiEvent.OnAddItem -> onAddItemToList(uiEvent.name)
        }
    }

    private fun onSearchTermChanged(newSearchTerm: String) {
        viewModelScope.launch(dispatchersProvider.Main) {
            nameSuggestionRepository.findAllMatching(newSearchTerm).collectLatest { suggestions ->
                _screenState.update { it.copy(searchTerm = newSearchTerm, suggestions = suggestions) }
            }
        }
    }

    private fun onAddItemToList(name: String) {
        viewModelScope.launch(dispatchersProvider.Main) {
            shoppingList
                .map { ShoppingItem(name = name, isChecked = false, list = it) }
                .collectLatest {
                    shoppingItemRepository.create(it)
                    nameSuggestionRepository.create(it.name)
                }
            _screenState.update { it.copy(toastMessage = "$name added to shopping list") }
        }
    }

    class Factory(
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
