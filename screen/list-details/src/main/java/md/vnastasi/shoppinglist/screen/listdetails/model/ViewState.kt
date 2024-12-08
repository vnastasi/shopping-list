package md.vnastasi.shoppinglist.screen.listdetails.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingItem

@Stable
sealed class ViewState {

    data object Loading : ViewState()

    data class Ready(
        val shoppingListId: Long,
        val shoppingListName: String,
        val listOfShoppingItems: ImmutableList<ShoppingItem>
    ) : ViewState()
}
