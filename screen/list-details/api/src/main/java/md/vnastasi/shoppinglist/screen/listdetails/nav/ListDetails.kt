package md.vnastasi.shoppinglist.screen.listdetails.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ListDetails(val shoppingListId: Long): NavKey
