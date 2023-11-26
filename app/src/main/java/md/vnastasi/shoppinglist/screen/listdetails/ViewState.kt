package md.vnastasi.shoppinglist.screen.listdetails

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.ShoppingItem

@Stable
data class ViewState(
    val shoppingListId: Long,
    val shoppingListName: String,
    val listOfShoppingItems: ImmutableList<ShoppingItem>
) {

    companion object {

        val Init = ViewState(-1L, "", persistentListOf())
    }
}
