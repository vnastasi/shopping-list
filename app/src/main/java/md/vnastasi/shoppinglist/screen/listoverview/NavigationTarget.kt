package md.vnastasi.shoppinglist.screen.listoverview

sealed class NavigationTarget {

    data class ShoppingListDetails(val id: Long) : NavigationTarget()

    data object ShoppingListForm : NavigationTarget()
}
