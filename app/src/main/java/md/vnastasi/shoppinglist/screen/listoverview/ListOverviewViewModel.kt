package md.vnastasi.shoppinglist.screen.listoverview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository

class ListOverviewViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    val screenState: StateFlow<ScreenState> =
        shoppingListRepository.getAvailableLists()
            .map { if (it.isEmpty()) ScreenState.NoEntries else ScreenState.AvailableEntries(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), ScreenState.Loading)

    private val _navigationTarget = MutableSharedFlow<NavigationTarget>()
    val navigationTarget: SharedFlow<NavigationTarget> = _navigationTarget.asSharedFlow()

    fun onListItemDelete(shoppingList: ShoppingList) {
        Log.d("EVENTS", "Shopping list <$shoppingList> deleted")
        viewModelScope.launch(Dispatchers.IO) {
            shoppingListRepository.delete(shoppingList)
        }
    }

    fun onListItemClick(shoppingList: ShoppingList) {
        Log.d("EVENTS", "Shopping list <$shoppingList> clicked")
        viewModelScope.launch {
            _navigationTarget.emit(NavigationTarget.ShoppingListDetails(shoppingList.id))
        }
    }

    fun onAddShoppingListClicked() {
        Log.d("EVENTS", "New shopping list")
        viewModelScope.launch {
            _navigationTarget.emit(NavigationTarget.ShoppingListForm)
        }
    }

    fun onSaveNewShoppingList(name: String) {
        viewModelScope.launch {
            shoppingListRepository.create(ShoppingList(name = name))
        }
    }

    class Factory(
        private val shoppingListRepository: ShoppingListRepository
    ) : ViewModelProvider.Factory by viewModelFactory({
        initializer {
            ListOverviewViewModel(shoppingListRepository)
        }
    })
}
