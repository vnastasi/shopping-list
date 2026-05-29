package md.vnastasi.shoppinglist.screen.additems.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class AddItems(val shoppingListId: Long): NavKey
