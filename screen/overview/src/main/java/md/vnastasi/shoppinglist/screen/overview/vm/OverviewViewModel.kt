package md.vnastasi.shoppinglist.screen.overview.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.toast.ToastMessage
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject internal constructor(
    private val shoppingListRepository: ShoppingListRepository,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), OverviewViewModelSpec {

    private val navigationTarget = MutableStateFlow<NavigationTarget?>(null)
    private val toastMessage = MutableStateFlow<ToastMessage?>(null)

    override val viewState: StateFlow<ViewState> = combine(
        shoppingListRepository.findAll().map { it.toImmutableList() },
        navigationTarget,
        toastMessage,
        ::createViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT),
        initialValue = ViewState.Loading
    )

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

    private fun createViewState(
        list: ImmutableList<ShoppingListDetails>,
        navigationTarget: NavigationTarget?,
        toastMessage: ToastMessage?
    ): ViewState =
        if (list.isEmpty()) ViewState.Empty(navigationTarget, toastMessage) else ViewState.Ready(list, navigationTarget, toastMessage)

    private fun onShoppingListDeleted(shoppingListDetails: ShoppingListDetails) {
        viewModelScope.launch {
            shoppingListRepository.delete(shoppingListDetails.toShoppingList())
        }
    }

    private fun onShoppingListSelected(shoppingListDetails: ShoppingListDetails) {
        navigationTarget.value = NavigationTarget.ShoppingListDetails(shoppingListDetails.id)
    }

    private fun onAddNewShoppingList() {
        navigationTarget.value = NavigationTarget.ShoppingListForm
    }

    private fun onShoppingListSaved(name: String) {
        viewModelScope.launch {
            shoppingListRepository.create(ShoppingList(name = name))
            toastMessage.value = ToastMessage(textResourceId = R.string.toast_list_created, arguments = persistentListOf(name))
        }
    }

    private fun onNavigationPerformed() {
        navigationTarget.value = null
    }

    private fun onToastShown() {
        toastMessage.value = null
    }

    companion object {

        private const val FLOW_SUBSCRIPTION_TIMEOUT = 5_000L
    }
}
