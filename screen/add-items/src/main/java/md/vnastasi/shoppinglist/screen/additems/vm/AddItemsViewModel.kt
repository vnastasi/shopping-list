package md.vnastasi.shoppinglist.screen.additems.vm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.repository.NameSuggestionRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.toast.ToastMessage

@HiltViewModel(assistedFactory = AddItemsViewModel.Factory::class)
class AddItemsViewModel @AssistedInject internal constructor(
    @Assisted private val shoppingListId: Long,
    private val nameSuggestionRepository: NameSuggestionRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val shoppingItemRepository: ShoppingItemRepository,
    coroutineScope: CoroutineScope
) : ViewModel(coroutineScope), AddItemsViewModelSpec {

    private val _viewState = MutableStateFlow(ViewState.init())
    override val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    override val searchTermState: MutableState<String> = mutableStateOf("")

    override fun onUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.SearchTermChanged -> onSearchTermChanged()
            is UiEvent.ItemAddedToList -> onItemAddedToList(uiEvent.name)
            is UiEvent.SuggestionDeleted -> onSuggestionDeleted(uiEvent.suggestion)
            is UiEvent.ToastShown -> onToastShown()
        }
    }

    private fun onSearchTermChanged() {
        viewModelScope.launch {
            _viewState.update { viewState ->
                viewState.copy(
                    suggestions = nameSuggestionRepository.findAllMatching(searchTermState.value.trim()).toImmutableList()
                )
            }
        }
    }

    private fun onItemAddedToList(name: String) {
        val sanitisedName = name.trim()
        if (sanitisedName.isBlank()) return
        viewModelScope.launch {
            shoppingListRepository.findById(shoppingListId)
                .map { ShoppingItem(name = sanitisedName, isChecked = false, list = it) }
                .collectLatest { shoppingItem ->
                    shoppingItemRepository.create(shoppingItem)
                    _viewState.update { viewState ->
                        val toastMessage = ToastMessage(
                            textResourceId = R.string.toast_item_added,
                            arguments = persistentListOf(sanitisedName)
                        )
                        viewState.copy(toastMessage = toastMessage)
                    }
                    searchTermState.value = ""
                }
        }
    }

    private fun onSuggestionDeleted(suggestion: NameSuggestion) {
        viewModelScope.launch {
            nameSuggestionRepository.delete(suggestion)
            _viewState.update { viewState ->
                val toastMessage = ToastMessage(
                    textResourceId = R.string.toast_suggestion_removed,
                    arguments = persistentListOf(suggestion.name)
                )
                viewState.copy(
                    suggestions = nameSuggestionRepository.findAllMatching(searchTermState.value).toImmutableList(),
                    toastMessage = toastMessage
                )
            }
        }
    }

    private fun onToastShown() {
        viewModelScope.launch {
            _viewState.update { viewState ->
                viewState.copy(toastMessage = null)
            }
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(shoppingListId: Long): AddItemsViewModel
    }
}
