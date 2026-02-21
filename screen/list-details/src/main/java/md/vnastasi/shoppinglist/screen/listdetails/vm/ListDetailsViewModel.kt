package md.vnastasi.shoppinglist.screen.listdetails.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.listdetails.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.coroutine.FLOW_SUBSCRIPTION_TIMEOUT

@HiltViewModel(assistedFactory = ListDetailsViewModel.Factory::class)
class ListDetailsViewModel @AssistedInject internal constructor(
    @Assisted private val shoppingListId: Long,
    private val shoppingItemRepository: ShoppingItemRepository,
    shoppingListRepository: ShoppingListRepository,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), ListDetailsViewModelSpec {

    private val _navigationTarget = MutableStateFlow<NavigationTarget?>(null)

    override val viewState: StateFlow<ViewState> = combine(
        flow = shoppingListRepository.findById(shoppingListId),
        flow2 = shoppingItemRepository.findAll(shoppingListId),
        flow3 = _navigationTarget,
        transform = ::createViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT),
        initialValue = ViewState.Loading
    )

    override fun dispatch(event: UiEvent) {
        when (event) {
            is UiEvent.OnItemClicked -> onShoppingItemClicked(event.shoppingItem)
            is UiEvent.OnItemDeleted -> onShoppingItemDeleted(event.shoppingItem)
            is UiEvent.OnAddItemsClicked -> onAddItemsClicked()
            is UiEvent.OnBackClicked -> onBackClicked()
            is UiEvent.OnNavigationConsumed -> onNavigationConsumed()
        }
    }

    private fun onShoppingItemClicked(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            shoppingItemRepository.update(shoppingItem.copy(isChecked = !shoppingItem.isChecked))
        }
    }

    private fun onShoppingItemDeleted(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            shoppingItemRepository.delete(shoppingItem)
        }
    }

    private fun onAddItemsClicked() {
        _navigationTarget.update { NavigationTarget.AddItems(shoppingListId) }
    }

    private fun onBackClicked() {
        _navigationTarget.update { NavigationTarget.BackToOverview }
    }

    private fun onNavigationConsumed() {
        _navigationTarget.update { null }
    }

    private fun createViewState(
        shoppingList: ShoppingList,
        listOfShoppingItems: List<ShoppingItem>,
        navigationTarget: NavigationTarget?
    ): ViewState =
        if (listOfShoppingItems.isEmpty()) {
            ViewState.Empty(
                shoppingListId = shoppingList.id,
                shoppingListName = shoppingList.name,
                navigationTarget = navigationTarget
            )
        } else {
            ViewState.Ready(
                shoppingListId = shoppingList.id,
                shoppingListName = shoppingList.name,
                listOfShoppingItems = listOfShoppingItems.toImmutableList(),
                navigationTarget = navigationTarget
            )
        }

    @AssistedFactory
    interface Factory {

        fun create(shoppingListId: Long): ListDetailsViewModel
    }
}
