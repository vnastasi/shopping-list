package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails

@Immutable
sealed class ViewState {

    @Immutable
    data object Loading : ViewState()

    @Immutable
    data object Empty : ViewState()

    @Immutable
    data class Ready(val data: ImmutableList<ShoppingListDetails>) : ViewState()
}
