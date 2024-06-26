package md.vnastasi.shoppinglist.screen.overview.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.support.async.DispatchersProvider
import md.vnastasi.shoppinglist.support.ui.toast.ToastMessage

class ListOverviewViewModel internal constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel(), ListOverviewViewModelSpec {

    private val _screenState = MutableStateFlow(ViewState.Init)
    override val screenState: StateFlow<ViewState> = _screenState.asStateFlow()

    init {
        viewModelScope.launch(dispatchersProvider.Main) {
            shoppingListRepository.findAll().collectLatest { list ->
                _screenState.update { viewState ->
                    viewState.copy(shoppingLists = list.toImmutableList())
                }
            }
        }
    }

    override fun onUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.AddNewShoppingList -> onAddNewShoppingList()
            is UiEvent.ShoppingListSaved -> onShoppingListSaved(uiEvent.name)
            is UiEvent.ShoppingListSelected -> onShoppingListSelected(uiEvent.shoppingListDetails)
            is UiEvent.ShoppingListDeleted -> onShoppingListDeleted(uiEvent.shoppingListDetails)
            is UiEvent.NavigationPerformed -> onNavigationPerformed()
            is UiEvent.ToastShown -> onToastShown()
        }
    }

    private fun onShoppingListDeleted(shoppingListDetails: ShoppingListDetails) {
        viewModelScope.launch(dispatchersProvider.IO) {
            shoppingListRepository.delete(shoppingListDetails.toShoppingList())
        }
    }

    private fun onShoppingListSelected(shoppingListDetails: ShoppingListDetails) {
        viewModelScope.launch(dispatchersProvider.Main) {
            _screenState.update { viewState ->
                viewState.copy(navigationTarget = NavigationTarget.ShoppingListDetails(shoppingListDetails.id))
            }
        }
    }

    private fun onAddNewShoppingList() {
        viewModelScope.launch(dispatchersProvider.Main) {
            _screenState.update { viewState ->
                viewState.copy(navigationTarget = NavigationTarget.ShoppingListForm)
            }
        }
    }

    private fun onShoppingListSaved(name: String) {
        viewModelScope.launch(dispatchersProvider.Main) {
            shoppingListRepository.create(ShoppingList(name = name))
            _screenState.update { viewState ->
                viewState.copy(toastMessage = ToastMessage(textResourceId = R.string.toast_list_created, arguments = persistentListOf(name)))
            }
        }
    }

    private fun onNavigationPerformed() {
        viewModelScope.launch(dispatchersProvider.Main) {
            _screenState.update { viewState ->
                viewState.copy(navigationTarget = null)
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
        private val shoppingListRepository: ShoppingListRepository,
        private val dispatchersProvider: DispatchersProvider
    ) : ViewModelProvider.Factory by viewModelFactory({
        initializer {
            ListOverviewViewModel(shoppingListRepository, dispatchersProvider)
        }
    })
}
