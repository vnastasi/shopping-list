package md.vnastasi.shoppinglist.screen.listdetails.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingItem

@Stable
sealed class ViewState {

    abstract val shouldShowBackButton: Boolean

    data class Loading(
        override val shouldShowBackButton: Boolean
    ) : ViewState()

    data class Empty(
        override val shouldShowBackButton: Boolean,
        val shoppingListId: Long,
        val shoppingListName: String
    ) : ViewState()

    data class Ready(
        override val shouldShowBackButton: Boolean,
        val shoppingListId: Long,
        val shoppingListName: String,
        val listOfShoppingItems: ImmutableList<ShoppingItem>
    ) : ViewState()
}
