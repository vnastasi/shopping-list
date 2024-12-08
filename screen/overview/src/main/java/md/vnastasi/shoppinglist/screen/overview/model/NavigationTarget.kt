package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Stable

@Stable
sealed class NavigationTarget {

    data class ShoppingListDetails(val id: Long) : NavigationTarget()

    data object ShoppingListForm : NavigationTarget()
}
