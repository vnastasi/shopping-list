package md.vnastasi.shoppinglist.screen.main

sealed class NavigationTarget {

    data class ShoppingListDetails(val id: Long) : NavigationTarget()

    data object ShoppingListForm : NavigationTarget()
}
