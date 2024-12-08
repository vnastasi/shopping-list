package md.vnastasi.shoppinglist.nav

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object Overview : Routes()

    @Serializable
    data class ListDetails(val shoppingListId: Long) : Routes()

    @Serializable
    data class AddItems(val shoppingListId: Long) : Routes()
}
