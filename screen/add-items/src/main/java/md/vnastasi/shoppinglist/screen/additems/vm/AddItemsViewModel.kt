package md.vnastasi.shoppinglist.screen.additems.vm

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.repository.NameSuggestionRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.additems.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.coroutine.FLOW_SUBSCRIPTION_TIMEOUT
import md.vnastasi.shoppinglist.screen.shared.toast.ToastMessage

@HiltViewModel(assistedFactory = AddItemsViewModel.Factory::class)
class AddItemsViewModel @AssistedInject internal constructor(
    @Assisted private val shoppingListId: Long,
    private val nameSuggestionRepository: NameSuggestionRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val shoppingItemRepository: ShoppingItemRepository,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), AddItemsViewModelSpec {

    private val _triggerCounter = MutableStateFlow(0)

    private val _suggestions = snapshotFlow { searchTermTextFieldState.text.toString() }.flatMapLatest { nameSuggestionRepository.findAllMatching(it) }

    private val _toastMessage = MutableStateFlow<ToastMessage?>(null)

    private val _navigationTarget = MutableStateFlow<NavigationTarget?>(null)

    override val searchTermTextFieldState: TextFieldState = TextFieldState(initialText = "")

    override val viewState: StateFlow<ViewState> = combine(
        flow = _triggerCounter,
        flow2 = _suggestions,
        flow3 = _toastMessage,
        flow4 = _navigationTarget,
        transform = { _, suggestions, toastMessage, navigationTarget -> createViewState(suggestions, toastMessage, navigationTarget) }
    ).stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT),
        initialValue = ViewState.init()
    )

    override fun dispatch(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.OnItemAddedToList -> onItemAddedToList(uiEvent.name)
            is UiEvent.OnSuggestionDeleted -> onSuggestionDeleted(uiEvent.suggestion)
            is UiEvent.OnToastShown -> onToastShown()
            is UiEvent.OnBackClicked -> onBackClicked()
            is UiEvent.OnNavigationConsumed -> onNavigationConsumed()
        }
    }

    private fun createViewState(
        suggestions: List<NameSuggestion>,
        toastMessage: ToastMessage?,
        navigationTarget: NavigationTarget?
    ): ViewState = ViewState(
        suggestions = suggestions.toImmutableList(),
        toastMessage = toastMessage,
        navigationTarget = navigationTarget
    )

    private fun onItemAddedToList(name: String) {
        val sanitisedName = name.trim()
        if (sanitisedName.isBlank()) return
        viewModelScope.launch {
            shoppingListRepository.findById(shoppingListId)
                .map { ShoppingItem(name = sanitisedName, isChecked = false, list = it) }
                .collectLatest { shoppingItem ->
                    shoppingItemRepository.create(shoppingItem)
                    _toastMessage.update {
                        ToastMessage(
                            textResourceId = R.string.toast_item_added,
                            arguments = persistentListOf(sanitisedName)
                        )
                    }
                    searchTermTextFieldState.clearText()
                }
        }
    }

    private fun onSuggestionDeleted(suggestion: NameSuggestion) {
        viewModelScope.launch {
            nameSuggestionRepository.delete(suggestion)
            _toastMessage.update {
                ToastMessage(
                    textResourceId = R.string.toast_suggestion_removed,
                    arguments = persistentListOf(suggestion.name)
                )
            }
            _triggerCounter.update { it + 1 }
        }
    }

    private fun onToastShown() {
        _toastMessage.update { null }
    }

    private fun onBackClicked() {
        _navigationTarget.update { NavigationTarget.BackToListDetails }
    }

    private fun onNavigationConsumed() {
        _navigationTarget.update { null }
    }

    @AssistedFactory
    interface Factory {

        fun create(shoppingListId: Long): AddItemsViewModel
    }
}
