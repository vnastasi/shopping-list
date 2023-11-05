package md.vnastasi.shoppinglist.screen.listoverview

import md.vnastasi.shoppinglist.domain.model.ShoppingList

sealed interface UiEvent {

    data class DeleteShoppingList(val shoppingList: ShoppingList) : UiEvent

    data class SelectShoppingList(val shoppingList: ShoppingList): UiEvent

    data object AddNewShoppingList : UiEvent

    data class SaveShoppingList(val name: String) : UiEvent
}
