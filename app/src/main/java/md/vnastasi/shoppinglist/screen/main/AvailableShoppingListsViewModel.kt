package md.vnastasi.shoppinglist.screen.main

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository

class AvailableShoppingListsViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    val screenState = shoppingListRepository.getAvailableLists()
        .map { if (it.isEmpty()) ScreenState.NoEntries else ScreenState.AvailableEntries(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), ScreenState.Loading)

    fun onDelete(shoppingList: ShoppingList) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingListRepository.delete(shoppingList)
        }
    }

    class Factory(
        private val shoppingListRepository: ShoppingListRepository
    ) : AbstractSavedStateViewModelFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T =
            AvailableShoppingListsViewModel(shoppingListRepository) as T
    }
}
