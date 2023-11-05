package md.vnastasi.shoppinglist.screen.listoverview

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.ShoppingList

@Stable
data class ViewState(
    val shoppingLists: ImmutableList<ShoppingList>
) {

    companion object {

        val Init = ViewState(persistentListOf())
    }
}
