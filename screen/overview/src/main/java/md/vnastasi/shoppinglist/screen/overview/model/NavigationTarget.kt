package md.vnastasi.shoppinglist.screen.overview.model

sealed interface NavigationTarget {

    data class AddOrEditList(val shoppingListId: Long?) : NavigationTarget

    data class ListDetails(val shoppingListId: Long) : NavigationTarget
}
