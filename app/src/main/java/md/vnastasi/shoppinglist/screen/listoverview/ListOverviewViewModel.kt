package md.vnastasi.shoppinglist.screen.listoverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.collections.immutable.ImmutableList
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
import md.vnastasi.shoppinglist.support.state.ScreenState

class ListOverviewViewModel(
    private val shoppingListRepository: ShoppingListRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    val screenState: StateFlow<ScreenState<ImmutableList<ShoppingList>, Nothing>> =
        shoppingListRepository.findAll()
            .map { if (it.isEmpty()) ScreenState.empty() else ScreenState.ready(it.toImmutableList()) }
            .stateIn(
                scope = viewModelScope + dispatchersProvider.MainImmediate,
                started = SharingStarted.WhileSubscribed(100L),
                initialValue = ScreenState.loading()
            )

    private val _navigationTarget = MutableSharedFlow<NavigationTarget>()
    val navigationTarget: SharedFlow<NavigationTarget> = _navigationTarget.asSharedFlow()

    fun onUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.OnAddNewShoppingListClicked -> onAddShoppingListClicked()
            is UiEvent.OnSaveNewShoppingList -> onSaveNewShoppingList(uiEvent.name)
            is UiEvent.OnShoppingListItemClicked -> onListItemClicked(uiEvent.shoppingList)
            is UiEvent.OnShoppingListItemDeleted -> onListItemDeleted(uiEvent.shoppingList)
        }
    }

    private fun onListItemDeleted(shoppingList: ShoppingList) {
        viewModelScope.launch(dispatchersProvider.IO) {
            shoppingListRepository.delete(shoppingList)
        }
    }

    private fun onListItemClicked(shoppingList: ShoppingList) {
        viewModelScope.launch(dispatchersProvider.Main) {
            _navigationTarget.emit(NavigationTarget.ShoppingListDetails(shoppingList.id))
        }
    }

    private fun onAddShoppingListClicked() {
        viewModelScope.launch(dispatchersProvider.Main) {
            _navigationTarget.emit(NavigationTarget.ShoppingListForm)
        }
    }

    private fun onSaveNewShoppingList(name: String) {
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
