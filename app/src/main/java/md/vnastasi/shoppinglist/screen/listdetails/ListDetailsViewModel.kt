package md.vnastasi.shoppinglist.screen.listdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.support.state.ScreenState

class ListDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    val screenState: StateFlow<ScreenState<ListDetails, Nothing>> =
        savedStateHandle.getStateFlow<Long?>(ARG_KEY_SHOPPING_LIST_ID, null)
            .filterNotNull()
            .flatMapLatest(shoppingListRepository::getListById)
            .map { shoppingList ->
                val details = ListDetails(
                    id = shoppingList.id,
                    name = shoppingList.name,
                    shoppingItems = emptyList() // TODO fill in later
                )
                ScreenState.ready(details)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), ScreenState.loading())

    class Factory(
        private val shoppingListRepository: ShoppingListRepository
    ) : ViewModelProvider.Factory by viewModelFactory({
        initializer {
            ListDetailsViewModel(createSavedStateHandle(), shoppingListRepository)
        }
    })

    companion object {

        const val ARG_KEY_SHOPPING_LIST_ID = "SHOPPING_LIST_ID"
    }
}
