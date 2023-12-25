package md.vnastasi.shoppinglist.screen.overview

import md.vnastasi.shoppinglist.domain.model.ShoppingList

sealed interface UiEvent {

    data class ShoppingListDeleted(val shoppingList: ShoppingList) : UiEvent

    data class ShoppingListSelected(val shoppingList: ShoppingList) : UiEvent

    data object AddNewShoppingList : UiEvent

    data class ShoppingListSaved(val name: String) : UiEvent

    data object NavigationPerformed : UiEvent

    data object ToastShown : UiEvent
}
