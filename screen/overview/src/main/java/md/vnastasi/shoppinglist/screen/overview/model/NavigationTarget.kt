package md.vnastasi.shoppinglist.screen.overview.model

sealed class NavigationTarget {

    data class ShoppingListDetails(val id: Long) : NavigationTarget()

    data object ShoppingListForm : NavigationTarget()
}
