package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails

@Immutable
sealed class ViewState {

    abstract val navigationTarget: NavigationTarget?

    @Immutable
    data object Loading : ViewState() {

        override val navigationTarget: NavigationTarget? = null
    }

    @Immutable
    data class Empty(
        override val navigationTarget: NavigationTarget?
    ) : ViewState()

    @Immutable
    data class Ready(
        val data: ImmutableList<ShoppingListDetails>,
        override val navigationTarget: NavigationTarget?
    ) : ViewState()
}
