package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Immutable
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails

@Immutable
sealed interface UiEvent {

    @Immutable
    data class OnShoppingListDeleted(val shoppingList: ShoppingListDetails) : UiEvent

    @Immutable
    data class OnShoppingListsReordered(val reorderedList: List<ShoppingListDetailsUiModel>) : UiEvent

    @Immutable
    data class OnShoppingListSelected(val shoppingList: ShoppingListDetails) : UiEvent

    @Immutable
    data class OnShoppingListEdited(val shoppingList: ShoppingListDetails) : UiEvent

    @Immutable
    data class OnSwipeToRevealStateChanged(val shoppingList: ShoppingListDetailsUiModel, val newState: SwipeToRevealState) : UiEvent

    @Immutable
    data object OnAddNewShoppingList : UiEvent
}
