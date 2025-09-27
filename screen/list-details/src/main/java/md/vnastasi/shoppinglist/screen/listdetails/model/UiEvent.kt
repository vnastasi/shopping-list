package md.vnastasi.shoppinglist.screen.listdetails.model

import md.vnastasi.shoppinglist.domain.model.ShoppingItem

sealed interface UiEvent {

    data class ShoppingItemClicked(val shoppingItem: ShoppingItem): UiEvent

    data class ShoppingItemDeleted(val shoppingItem: ShoppingItem): UiEvent

    data class ShoppingItemsReordered(val shoppingItems: List<ShoppingItem>): UiEvent
}
