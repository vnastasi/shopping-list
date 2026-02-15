package md.vnastasi.shoppinglist.screen.overview.model

import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails

sealed interface UiEvent {

    data class OnShoppingListDeleted(val shoppingList: ShoppingListDetails) : UiEvent

    data class OnShoppingListsReordered(val reorderedList: List<ShoppingListDetails>) : UiEvent

    data class OnShoppingListSelected(val shoppingList: ShoppingListDetails) : UiEvent

    data class OnShoppingListEdited(val shoppingList: ShoppingListDetails) : UiEvent

    data object OnAddNewShoppingList : UiEvent

    data object OnNavigationConsumed : UiEvent
}
