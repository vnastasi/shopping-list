package md.vnastasi.shoppinglist.screen.overview.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
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
import md.vnastasi.shoppinglist.screen.overview.model.ShoppingListUiModel
import md.vnastasi.shoppinglist.screen.overview.model.SwipeToRevealState
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.vm.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject internal constructor(
    private val shoppingListRepository: ShoppingListRepository,
    savedStateHandle: SavedStateHandle,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), OverviewViewModelSpec {

    private val _swipeToRevealStates = savedStateHandle.getMutableStateFlow<Map<Long, SwipeToRevealState>>(KEY_SWIPE_TO_REVEAL_STATES, emptyMap())

    private val _shoppingLists: Flow<List<ShoppingListDetails>> = shoppingListRepository.findAll()

    override val viewState: StateFlow<ViewState> = combine(
        flow = _shoppingLists,
        flow2 = _swipeToRevealStates,
        transform = ::createViewState
    ).asStateFlow(ViewState.Loading)

    private val _effect = Channel<Effect>()
    override val effect: Flow<Effect> = _effect.receiveAsFlow()

    override fun dispatch(event: UiEvent) {
        when (event) {
            is UiEvent.OnShoppingListDeleted -> onShoppingListDeleted(event.shoppingListUiModel.shoppingList)
            is UiEvent.OnShoppingListsReordered -> onShoppingListsReordered(event.reorderedList.map { it.shoppingList })
            is UiEvent.OnAddNewShoppingList -> onAddNewShoppingList()
            is UiEvent.OnShoppingListEdited -> onShoppingListEdited(event.shoppingListUiModel.shoppingList.id)
            is UiEvent.OnShoppingListSelected -> onShoppingListSelected(event.shoppingListUiModel.shoppingList.id)
            is UiEvent.OnSwipeToRevealStateChanged -> onSwipeToRevealStateChanged(event.shoppingListUiModel.shoppingList, event.newState)
        }
    }

    private fun createViewState(
        shoppingLists: List<ShoppingListDetails>,
        swipeToRevealStates: Map<Long, SwipeToRevealState>
    ): ViewState {
        if (shoppingLists.isEmpty()) {
            return ViewState.Empty
        }

        val uiModelList = shoppingLists
            .map { shoppingList ->
                ShoppingListUiModel(
                    shoppingList = shoppingList,
                    swipeToRevealState = swipeToRevealStates.getOrDefault(shoppingList.id, SwipeToRevealState.Content)
                )
            }
        return ViewState.Ready(uiModelList.toImmutableList())
    }

    private fun onSwipeToRevealStateChanged(
        shoppingListDetails: ShoppingListDetails,
        newState: SwipeToRevealState
    ) {
        _swipeToRevealStates.update { it.plus(shoppingListDetails.id to newState) }
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

    companion object {

        const val KEY_SWIPE_TO_REVEAL_STATES = "SWIPE_TO_REVEAL_STATES"
    }
}
