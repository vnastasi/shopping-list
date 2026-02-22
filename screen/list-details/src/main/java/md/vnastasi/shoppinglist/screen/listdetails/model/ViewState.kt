package md.vnastasi.shoppinglist.screen.listdetails.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingItem

@Immutable
sealed class ViewState {

    abstract val navigationTarget: NavigationTarget?

    @Immutable
    data object Loading : ViewState() {

        override val navigationTarget: NavigationTarget? = null
    }

    @Immutable
    data class Empty(
        val shoppingListId: Long,
        val shoppingListName: String,
        override val navigationTarget: NavigationTarget?
    ) : ViewState()

    @Immutable
    data class Ready(
        val shoppingListId: Long,
        val shoppingListName: String,
        val listOfShoppingItems: ImmutableList<ShoppingItem>,
        override val navigationTarget: NavigationTarget?
    ) : ViewState()
}
