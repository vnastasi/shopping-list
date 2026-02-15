package md.vnastasi.shoppinglist.screen.managelist.model

sealed interface NavigationTarget {

    data object CloseSheet : NavigationTarget
}
