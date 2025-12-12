package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails

@Stable
sealed class ViewState {

    data object Loading : ViewState()

    data object Empty : ViewState()

    data class Ready(
        val shoppingLists: ImmutableList<ShoppingListDetails>,
    ) : ViewState()
}
