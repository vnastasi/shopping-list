package md.vnastasi.shoppinglist.screen.overview

sealed class NavigationTarget {

    data class ShoppingListDetails(val id: Long) : NavigationTarget()

    data object ShoppingListForm : NavigationTarget()
}
