package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface UiEvent {

    @Immutable
    data class OnShoppingListDeleted(val shoppingListUiModel: ShoppingListUiModel) : UiEvent

    @Immutable
    data class OnShoppingListsReordered(val reorderedList: List<ShoppingListUiModel>) : UiEvent

    @Immutable
    data class OnShoppingListSelected(val shoppingListUiModel: ShoppingListUiModel) : UiEvent

    @Immutable
    data class OnShoppingListEdited(val shoppingListUiModel: ShoppingListUiModel) : UiEvent

    @Immutable
    data class OnSwipeToRevealStateChanged(
        val shoppingListUiModel: ShoppingListUiModel,
        val newState: SwipeToRevealState
    ) : UiEvent

    @Immutable
    data object OnAddNewShoppingList : UiEvent
}
