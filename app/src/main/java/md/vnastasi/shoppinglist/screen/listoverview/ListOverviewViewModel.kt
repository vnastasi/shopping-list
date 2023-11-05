package md.vnastasi.shoppinglist.screen.listoverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.support.async.DispatchersProvider

class ListOverviewViewModel(
    private val shoppingListRepository: ShoppingListRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    val screenState: StateFlow<ViewState> =
        shoppingListRepository.findAll()
            .map { ViewState(it.toImmutableList()) }
            .stateIn(
                scope = viewModelScope + dispatchersProvider.MainImmediate,
                started = SharingStarted.Eagerly,
                initialValue = ViewState.Init
            )

    private val _navigationTarget = MutableSharedFlow<NavigationTarget>()
    val navigationTarget: SharedFlow<NavigationTarget> = _navigationTarget.asSharedFlow()

    fun onUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.AddNewShoppingList -> onAddNewShoppingList()
            is UiEvent.SaveShoppingList -> onSaveShoppingList(uiEvent.name)
            is UiEvent.SelectShoppingList -> onSelectShoppingList(uiEvent.shoppingList)
            is UiEvent.DeleteShoppingList -> onDeleteShoppingList(uiEvent.shoppingList)
        }
    }

    private fun onDeleteShoppingList(shoppingList: ShoppingList) {
        viewModelScope.launch(dispatchersProvider.IO) {
            shoppingListRepository.delete(shoppingList)
        }
    }

    private fun onSelectShoppingList(shoppingList: ShoppingList) {
        viewModelScope.launch(dispatchersProvider.Main) {
            _navigationTarget.emit(NavigationTarget.ShoppingListDetails(shoppingList.id))
        }
    }

    private fun onAddNewShoppingList() {
        viewModelScope.launch(dispatchersProvider.Main) {
            _navigationTarget.emit(NavigationTarget.ShoppingListForm)
        }
    }

    private fun onSaveShoppingList(name: String) {
        viewModelScope.launch(dispatchersProvider.Main) {
            shoppingListRepository.create(ShoppingList(name = name))
        }
    }

    class Factory(
        private val shoppingListRepository: ShoppingListRepository,
        private val dispatchersProvider: DispatchersProvider
    ) : ViewModelProvider.Factory by viewModelFactory({
        initializer {
            ListOverviewViewModel(shoppingListRepository, dispatchersProvider)
        }
    })
}
