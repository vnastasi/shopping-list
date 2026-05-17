package md.vnastasi.shoppinglist.screen.overview.nav

import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget

interface OverviewScreenNavigator {

    fun toListDetails(shoppingListId: Long)

    fun openManageListSheet(shoppingListId: Long?)
}

internal fun OverviewScreenNavigator.navigate(target: NavigationTarget) {
    when (target) {
        is NavigationTarget.AddOrEditList -> {
            openManageListSheet(target.shoppingListId)
        }

        is NavigationTarget.ListDetails -> {
            toListDetails(target.shoppingListId)
        }
    }
}
