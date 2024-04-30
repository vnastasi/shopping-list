package md.vnastasi.shoppinglist.screen.overview.model

import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails

sealed interface UiEvent {

    data class ShoppingListDeleted(val shoppingListDetails: ShoppingListDetails) : UiEvent

    data class ShoppingListSelected(val shoppingListDetails: ShoppingListDetails) : UiEvent

    data object AddNewShoppingList : UiEvent

    data class ShoppingListSaved(val name: String) : UiEvent

    data object NavigationPerformed : UiEvent

    data object ToastShown : UiEvent
}
