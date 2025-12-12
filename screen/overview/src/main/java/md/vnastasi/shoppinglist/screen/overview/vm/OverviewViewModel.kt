package md.vnastasi.shoppinglist.screen.overview.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.coroutine.FLOW_SUBSCRIPTION_TIMEOUT
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject internal constructor(
    private val shoppingListRepository: ShoppingListRepository,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), OverviewViewModelSpec {

    override val viewState: StateFlow<ViewState> = shoppingListRepository.findAll()
        .map { it.toImmutableList() }
        .map { if (it.isEmpty()) ViewState.Empty else ViewState.Ready(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT),
            initialValue = ViewState.Loading
        )

    override fun onUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.ShoppingListDeleted -> onShoppingListDeleted(uiEvent.shoppingListDetails)
        }
    }

    private fun onShoppingListDeleted(shoppingListDetails: ShoppingListDetails) {
        viewModelScope.launch {
            shoppingListRepository.delete(shoppingListDetails.toShoppingList())
        }
    }
}
