package md.vnastasi.shoppinglist.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

internal sealed class Route: NavKey {

    @Serializable
    data object Overview : Route()

    @Serializable
    data class ManageList(val shoppingListId: Long?) : Route()

    @Serializable
    data class ListDetails(val shoppingListId: Long) : Route()

    @Serializable
    data class AddItems(val shoppingListId: Long) : Route()
}
