package md.vnastasi.shoppinglist.screen.overview.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.coroutine.FLOW_SUBSCRIPTION_TIMEOUT
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject internal constructor(
    private val shoppingListRepository: ShoppingListRepository,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), OverviewViewModelSpec {

    private val _navigationTarget = MutableStateFlow<NavigationTarget?>(null)

    private val _shoppingLists = shoppingListRepository.findAll().map { it.toImmutableList() }

    override val viewState: StateFlow<ViewState> = combine(
        flow = _shoppingLists,
        flow2 = _navigationTarget,
        transform = ::createViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT),
        initialValue = ViewState.Loading
    )

    override fun dispatch(event: UiEvent) {
        when (event) {
            is UiEvent.OnShoppingListDeleted -> onShoppingListDeleted(event.shoppingList)
            is UiEvent.OnShoppingListsReordered -> onShoppingListsReordered(event.reorderedList)
            is UiEvent.OnAddNewShoppingList -> onAddNewShoppingList()
            is UiEvent.OnShoppingListEdited -> onShoppingListEdited(event.shoppingList.id)
            is UiEvent.OnShoppingListSelected -> onShoppingListSelected(event.shoppingList.id)
            is UiEvent.OnNavigationConsumed -> onNavigationConsumed()
        }
    }

    private fun createViewState(
        shoppingLists: ImmutableList<ShoppingListDetails>,
        navigationTarget: NavigationTarget?
    ): ViewState =
        if (shoppingLists.isEmpty()) {
            ViewState.Empty(navigationTarget)
        } else {
            ViewState.Ready(shoppingLists, navigationTarget)
        }

    private fun onShoppingListSelected(id: Long) {
        _navigationTarget.update { NavigationTarget.ListDetails(id) }
    }

    private fun onShoppingListEdited(id: Long) {
        _navigationTarget.update { NavigationTarget.AddOrEditList(id) }
    }

    private fun onAddNewShoppingList() {
        _navigationTarget.update { NavigationTarget.AddOrEditList(null) }
    }

    private fun onShoppingListDeleted(shoppingListDetails: ShoppingListDetails) {
        viewModelScope.launch {
            shoppingListRepository.delete(shoppingListDetails.toShoppingList())
        }
    }

    private fun onShoppingListsReordered(reorderedList: List<ShoppingListDetails>) {
        viewModelScope.launch {
            val listToUpdate = reorderedList.mapIndexed { index, shoppingListDetails ->
                ShoppingList(id = shoppingListDetails.id, name = shoppingListDetails.name, position = index.toLong())
            }
            shoppingListRepository.update(listToUpdate)
        }
    }

    private fun onNavigationConsumed() {
        _navigationTarget.update { null }
    }
}
