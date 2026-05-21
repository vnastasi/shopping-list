package md.vnastasi.shoppinglist.screen.overview.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.overview.model.Effect
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.vm.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject internal constructor(
    private val shoppingListRepository: ShoppingListRepository,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), OverviewViewModelSpec {

    override val viewState: StateFlow<ViewState> =
        shoppingListRepository.findAll()
            .map { it.toImmutableList() }
            .map { list -> if (list.isEmpty()) ViewState.Empty else ViewState.Ready(list) }
            .asStateFlow(ViewState.Loading)

    private val _effect = Channel<Effect>()
    override val effect: Flow<Effect> = _effect.receiveAsFlow()

    override fun dispatch(event: UiEvent) {
        when (event) {
            is UiEvent.OnShoppingListDeleted -> onShoppingListDeleted(event.shoppingList)
            is UiEvent.OnShoppingListsReordered -> onShoppingListsReordered(event.reorderedList)
            is UiEvent.OnAddNewShoppingList -> onAddNewShoppingList()
            is UiEvent.OnShoppingListEdited -> onShoppingListEdited(event.shoppingList.id)
            is UiEvent.OnShoppingListSelected -> onShoppingListSelected(event.shoppingList.id)
        }
    }

    private fun onShoppingListSelected(id: Long) {
        onNewEffect(Effect.Navigation(NavigationTarget.ListDetails(id)))
    }

    private fun onShoppingListEdited(id: Long) {
        onNewEffect(Effect.Navigation(NavigationTarget.AddOrEditList(id)))
    }

    private fun onAddNewShoppingList() {
        onNewEffect(Effect.Navigation(NavigationTarget.AddOrEditList(null)))
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

    private fun onNewEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
