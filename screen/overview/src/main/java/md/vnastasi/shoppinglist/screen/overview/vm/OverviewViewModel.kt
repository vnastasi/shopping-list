package md.vnastasi.shoppinglist.screen.overview.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.overview.model.Effect
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.overview.model.ShoppingListDetailsUiModel
import md.vnastasi.shoppinglist.screen.overview.model.SwipeToRevealState
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.vm.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject internal constructor(
    private val shoppingListRepository: ShoppingListRepository,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), OverviewViewModelSpec {

    private val _swipeToRevealState = MutableStateFlow<Map<Long, SwipeToRevealState>>(emptyMap())

    private val _shoppingLists: Flow<List<ShoppingListDetails>> = shoppingListRepository.findAll()

    override val viewState: StateFlow<ViewState> = combine(
        flow = _shoppingLists,
        flow2 = _swipeToRevealState,
        transform = { list, swipeToRevealStates ->
            val uiModelList = list.map { ShoppingListDetailsUiModel(it, swipeToRevealStates.getOrDefault(it.id, SwipeToRevealState.Content)) }.toImmutableList()
            if (list.isEmpty()) ViewState.Empty else ViewState.Ready(uiModelList)
        }
    ).asStateFlow(ViewState.Loading)

    private val _effect = Channel<Effect>()
    override val effect: Flow<Effect> = _effect.receiveAsFlow()

    override fun dispatch(event: UiEvent) {
        when (event) {
            is UiEvent.OnShoppingListDeleted -> onShoppingListDeleted(event.shoppingList)
            is UiEvent.OnShoppingListsReordered -> onShoppingListsReordered(event.reorderedList.map { it.shoppingListDetails })
            is UiEvent.OnAddNewShoppingList -> onAddNewShoppingList()
            is UiEvent.OnShoppingListEdited -> onShoppingListEdited(event.shoppingList.id)
            is UiEvent.OnShoppingListSelected -> onShoppingListSelected(event.shoppingList.id)
            is UiEvent.OnSwipeToRevealStateChanged -> onSwipeToRevealStateChanged(event.shoppingList.shoppingListDetails, event.newState)
        }
    }

    private fun onSwipeToRevealStateChanged(
        shoppingListDetails: ShoppingListDetails,
        newState: SwipeToRevealState
    ) {
        _swipeToRevealState.update { it.plus(shoppingListDetails.id to newState) }
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
