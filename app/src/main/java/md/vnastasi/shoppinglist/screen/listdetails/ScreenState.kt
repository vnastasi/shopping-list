package md.vnastasi.shoppinglist.screen.listdetails

import md.vnastasi.shoppinglist.db.model.ShoppingItem

sealed class ScreenState {

    data object Loading : ScreenState()

    data class Details(
        val id: Long,
        val name: String,
        val items: List<ShoppingItem>
    ): ScreenState()
}
