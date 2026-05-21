package md.vnastasi.shoppinglist.screen.listdetails.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingItem

@Immutable
sealed class ViewState {

    @Immutable
    data object Loading : ViewState()

    @Immutable
    data class Empty(
        val shoppingListId: Long,
        val shoppingListName: String,
    ) : ViewState()

    @Immutable
    data class Ready(
        val shoppingListId: Long,
        val shoppingListName: String,
        val listOfShoppingItems: ImmutableList<ShoppingItem>,
    ) : ViewState()
}
