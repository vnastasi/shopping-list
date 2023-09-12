package md.vnastasi.shoppinglist.screen.listoverview

import md.vnastasi.shoppinglist.domain.model.ShoppingList

sealed class ScreenState {

    data object Loading : ScreenState()

    data object NoEntries : ScreenState()

    data class AvailableEntries(val list: List<ShoppingList>) : ScreenState()
}
