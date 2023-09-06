package md.vnastasi.shoppinglist.screen.main

import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import kotlin.random.Random

class AvailableShoppingListsViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    val screenState = shoppingListRepository.getAvailableLists()
        .map { if (it.isEmpty()) ScreenState.NoEntries else ScreenState.AvailableEntries(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), ScreenState.Loading)

    val navigationTarget: SharedFlow<NavigationTarget> = MutableSharedFlow()

    fun onListItemDelete(shoppingList: ShoppingList) {
        Log.d("EVENTS", "Shopping list <$shoppingList> deleted")
        viewModelScope.launch(Dispatchers.IO) {
            shoppingListRepository.delete(shoppingList)
        }
    }

    fun onListItemClick(shoppingList: ShoppingList) {
        Log.d("EVENTS", "Shopping list <$shoppingList> clicked")
        viewModelScope.launch {
            (navigationTarget as MutableSharedFlow).emit(NavigationTarget.ShoppingListDetails(shoppingList.id))
        }
    }

    fun onAddShoppingListClicked() {
        Log.d("EVENTS", "New shopping list")
        viewModelScope.launch {
            (navigationTarget as MutableSharedFlow).emit(NavigationTarget.ShoppingListForm)
        }
    }

    fun onSaveNewShoppingList(name: String) {
        viewModelScope.launch {
            shoppingListRepository.create(ShoppingList(name = name))
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
