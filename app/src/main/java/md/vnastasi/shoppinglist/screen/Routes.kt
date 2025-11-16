package md.vnastasi.shoppinglist.screen

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Routes: NavKey {

    @Serializable
    data object Overview : Routes()

    @Serializable
    data class ListDetails(val shoppingListId: Long) : Routes()

    @Serializable
    data class AddItems(val shoppingListId: Long) : Routes()
}
