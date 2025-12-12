package md.vnastasi.shoppinglist.screen.overview.model

import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails

sealed interface UiEvent {

    data class ShoppingListDeleted(val shoppingListDetails: ShoppingListDetails) : UiEvent
}
