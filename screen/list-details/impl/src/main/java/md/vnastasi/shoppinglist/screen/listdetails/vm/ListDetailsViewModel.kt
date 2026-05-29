package md.vnastasi.shoppinglist.screen.listdetails.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.listdetails.model.Effect
import md.vnastasi.shoppinglist.screen.listdetails.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.vm.asStateFlow

@HiltViewModel(assistedFactory = ListDetailsViewModel.Factory::class)
internal class ListDetailsViewModel @AssistedInject internal constructor(
    @Assisted private val shoppingListId: Long,
    private val shoppingItemRepository: ShoppingItemRepository,
    shoppingListRepository: ShoppingListRepository,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), ListDetailsViewModelSpec {

    override val viewState: StateFlow<ViewState> = combine(
        flow = shoppingListRepository.findById(shoppingListId),
        flow2 = shoppingItemRepository.findAll(shoppingListId),
        transform = ::createViewState
    ).asStateFlow(initialValue = ViewState.Loading)

    private val _effect = Channel<Effect>()
    override val effect: Flow<Effect> = _effect.receiveAsFlow()

    override fun dispatch(event: UiEvent) {
        when (event) {
            is UiEvent.OnItemClicked -> onShoppingItemClicked(event.shoppingItem)
            is UiEvent.OnItemDeleted -> onShoppingItemDeleted(event.shoppingItem)
            is UiEvent.OnAddItemsClicked -> onAddItemsClicked()
            is UiEvent.OnItemsReordered -> onItemsReordered(event.reorderedList)
            is UiEvent.OnBackClicked -> onBackClicked()
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
        onNewEffect(Effect.Navigation(NavigationTarget.AddItems(shoppingListId)))
    }

    private fun onItemsReordered(reorderedList: List<ShoppingItem>) {
        viewModelScope.launch {
            val listToUpdate = reorderedList.reversed().mapIndexed { index, item -> item.copy(position = index.toLong()) }
            shoppingItemRepository.update(listToUpdate)
        }
    }

    private fun onBackClicked() {
        onNewEffect(Effect.Navigation(NavigationTarget.BackToOverview))
    }

    private fun createViewState(
        shoppingList: ShoppingList,
        listOfShoppingItems: List<ShoppingItem>,
    ): ViewState =
        if (listOfShoppingItems.isEmpty()) {
            ViewState.Empty(
                shoppingListId = shoppingList.id,
                shoppingListName = shoppingList.name,
            )
        } else {
            ViewState.Ready(
                shoppingListId = shoppingList.id,
                shoppingListName = shoppingList.name,
                listOfShoppingItems = listOfShoppingItems.toImmutableList(),
            )
        }

    private fun onNewEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(shoppingListId: Long): ListDetailsViewModel
    }
}
