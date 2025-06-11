package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.screen.shared.toast.ToastMessage

@Stable
sealed class ViewState {

    data object Loading : ViewState()

    data class Empty(
        val navigationTarget: NavigationTarget? = null,
        val toastMessage: ToastMessage? = null
    ) : ViewState()

    data class Ready(
        val shoppingLists: ImmutableList<ShoppingListDetails>,
        val navigationTarget: NavigationTarget? = null,
        val toastMessage: ToastMessage? = null
    ) : ViewState()
}
