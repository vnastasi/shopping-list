package md.vnastasi.shoppinglist.screen.listdetails

import md.vnastasi.shoppinglist.domain.model.ShoppingItem

data class ListDetails(
    val id: Long,
    val name: String,
    val shoppingItems: List<ShoppingItem>
)
