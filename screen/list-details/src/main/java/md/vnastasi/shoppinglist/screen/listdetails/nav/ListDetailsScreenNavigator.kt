package md.vnastasi.shoppinglist.screen.listdetails.nav

import md.vnastasi.shoppinglist.screen.listdetails.model.NavigationTarget

interface ListDetailsScreenNavigator {

    fun backToOverview()

    fun toAddItems(shoppingListId: Long)
}

internal fun ListDetailsScreenNavigator.navigate(target: NavigationTarget) {
    when (target) {
        is NavigationTarget.AddItems -> toAddItems(target.shoppingListId)
        is NavigationTarget.BackToOverview -> backToOverview()
    }
}
