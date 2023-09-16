package md.vnastasi.shoppinglist.screen.listdetails

import md.vnastasi.shoppinglist.domain.model.ShoppingItem

sealed interface UiEvent {

    data class OnShoppingListItemClicked(val shoppingItem: ShoppingItem): UiEvent
}
