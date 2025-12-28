package md.vnastasi.shoppinglist.screen.listdetails.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.coroutine.FLOW_SUBSCRIPTION_TIMEOUT

@HiltViewModel(assistedFactory = ListDetailsViewModel.Factory::class)
class ListDetailsViewModel @AssistedInject internal constructor(
    private val shoppingItemRepository: ShoppingItemRepository,
    @Assisted shoppingListId: Long,
    shoppingListRepository: ShoppingListRepository,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), ListDetailsViewModelSpec {

    override val viewState: StateFlow<ViewState> = combine(
        flow = shoppingListRepository.findById(shoppingListId),
        flow2 = shoppingItemRepository.findAll(shoppingListId),
        transform = ::createViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT),
        initialValue = ViewState.Loading
    )

    override fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.ShoppingItemClicked -> onShoppingItemClicked(event.shoppingItem)
            is UiEvent.ShoppingItemDeleted -> onShoppingItemDeleted(event.shoppingItem)
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

    private fun createViewState(
        shoppingList: ShoppingList,
        listOfShoppingItems: List<ShoppingItem>
    ): ViewState =
        if (listOfShoppingItems.isEmpty()) {
            ViewState.Empty(
                shoppingListId = shoppingList.id,
                shoppingListName = shoppingList.name
            )
        } else {
            ViewState.Ready(
                shoppingListId = shoppingList.id,
                shoppingListName = shoppingList.name,
                listOfShoppingItems = listOfShoppingItems.toImmutableList()
            )
        }

    @AssistedFactory
    interface Factory {

        fun create(shoppingListId: Long): ListDetailsViewModel
    }
}
