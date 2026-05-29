package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal sealed class ViewState {

    @Immutable
    data object Loading : ViewState()

    @Immutable
    data object Empty : ViewState()

    @Immutable
    data class Ready(val data: ImmutableList<ShoppingListUiModel>) : ViewState()
}
