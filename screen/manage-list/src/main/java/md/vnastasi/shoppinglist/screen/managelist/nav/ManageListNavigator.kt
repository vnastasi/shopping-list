package md.vnastasi.shoppinglist.screen.managelist.nav

import md.vnastasi.shoppinglist.screen.managelist.model.NavigationTarget

interface ManageListNavigator {

    fun closeSheet()
}

internal fun ManageListNavigator.navigate(target: NavigationTarget) {
    when (target) {
        is NavigationTarget.CloseSheet -> closeSheet()
    }
}
