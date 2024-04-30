package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.support.ui.toast.ToastMessage

@Stable
data class ViewState(
    val shoppingLists: ImmutableList<ShoppingListDetails>,
    val navigationTarget: NavigationTarget? = null,
    val toastMessage: ToastMessage? = null
) {

    companion object {

        val Init = ViewState(persistentListOf())
    }
}
