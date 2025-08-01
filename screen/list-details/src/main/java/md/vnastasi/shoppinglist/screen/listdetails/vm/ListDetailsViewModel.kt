package md.vnastasi.shoppinglist.screen.listdetails.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.support.async.DispatchersProvider

class ListDetailsViewModel internal constructor(
    private val savedStateHandle: SavedStateHandle,
    private val shoppingListRepository: ShoppingListRepository,
    private val shoppingItemRepository: ShoppingItemRepository,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel(), ListDetailsViewModelSpec {

    private val shoppingList = savedStateHandle.getStateFlow<Long?>(ARG_KEY_SHOPPING_LIST_ID, null)
        .filterNotNull()
        .flatMapLatest(shoppingListRepository::findById)

    private val listOfShoppingItems = savedStateHandle.getStateFlow<Long?>(ARG_KEY_SHOPPING_LIST_ID, null)
        .filterNotNull()
        .flatMapLatest(shoppingItemRepository::findAll)

    override val viewState: StateFlow<ViewState> = combine(
        shoppingList, listOfShoppingItems, ::createViewState
    ).stateIn(
        scope = viewModelScope + dispatchersProvider.MainImmediate,
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
        viewModelScope.launch(dispatchersProvider.IO) {
            shoppingItemRepository.update(shoppingItem.copy(isChecked = !shoppingItem.isChecked))
        }
    }

    private fun onShoppingItemDeleted(shoppingItem: ShoppingItem) {
        viewModelScope.launch(dispatchersProvider.IO) {
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

    class Factory internal constructor(
        private val shoppingListRepository: ShoppingListRepository,
        private val shoppingItemRepository: ShoppingItemRepository,
        private val dispatchersProvider: DispatchersProvider
    ) : ViewModelProvider.Factory by viewModelFactory({
        initializer {
            ListDetailsViewModel(
                savedStateHandle = createSavedStateHandle(),
                shoppingListRepository = shoppingListRepository,
                shoppingItemRepository = shoppingItemRepository,
                dispatchersProvider = dispatchersProvider
            )
        }
    })

    companion object {

        const val ARG_KEY_SHOPPING_LIST_ID = "shoppingListId"

        private const val FLOW_SUBSCRIPTION_TIMEOUT = 5_000L
    }
}
