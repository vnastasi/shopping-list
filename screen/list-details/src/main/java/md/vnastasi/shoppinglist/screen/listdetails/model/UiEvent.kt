package md.vnastasi.shoppinglist.screen.listdetails.model

import androidx.compose.runtime.Immutable
import md.vnastasi.shoppinglist.domain.model.ShoppingItem

@Immutable
sealed interface UiEvent {

    @Immutable
    data object OnBackClicked : UiEvent

    @Immutable
    data object OnAddItemsClicked : UiEvent

    @Immutable
    data class OnItemClicked(val shoppingItem: ShoppingItem): UiEvent

    @Immutable
    data class OnItemDeleted(val shoppingItem: ShoppingItem): UiEvent

    @Immutable
    data object OnNavigationConsumed : UiEvent
}
