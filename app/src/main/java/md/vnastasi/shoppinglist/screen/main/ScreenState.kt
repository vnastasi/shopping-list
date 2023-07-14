package md.vnastasi.shoppinglist.screen.main

import md.vnastasi.shoppinglist.domain.model.ShoppingList

sealed class ScreenState {

    object Loading : ScreenState()

    object NoEntries : ScreenState()

    data class AvailableEntries(val list: List<ShoppingList>) : ScreenState()
}
