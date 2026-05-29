package md.vnastasi.shoppinglist.screen.managelist.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ManageList(val shoppingListId: Long?): NavKey
