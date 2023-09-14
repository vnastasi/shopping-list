package md.vnastasi.shoppinglist.screen.listoverview

import md.vnastasi.shoppinglist.domain.model.ShoppingList

sealed interface UiEvent {

    data class OnShoppingListItemDeleted(val shoppingList: ShoppingList) : UiEvent

    data class OnShoppingListItemClicked(val shoppingList: ShoppingList): UiEvent

    data object OnAddNewShoppingListClicked : UiEvent

    data class OnSaveNewShoppingList(val name: String) : UiEvent
}
