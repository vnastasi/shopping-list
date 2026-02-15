package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails

@Stable
sealed class ViewState {

    abstract val navigationTarget: NavigationTarget?

    data object Loading : ViewState() {

        override val navigationTarget: NavigationTarget? = null
    }

    data class Empty(
        override val navigationTarget: NavigationTarget?
    ) : ViewState()

    data class Ready(
        val data: ImmutableList<ShoppingListDetails>,
        override val navigationTarget: NavigationTarget?
    ) : ViewState()
}
