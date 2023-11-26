package md.vnastasi.shoppinglist.screen.listdetails

import md.vnastasi.shoppinglist.domain.model.ShoppingItem

sealed interface UiEvent {

    data class ShoppingItemClicked(val shoppingItem: ShoppingItem): UiEvent
}
