package md.vnastasi.shoppinglist.screen.additems.nav

import md.vnastasi.shoppinglist.screen.additems.model.NavigationTarget

interface AddItemsScreenNavigator {

    fun backToListDetails()
}

internal fun AddItemsScreenNavigator.navigate(target: NavigationTarget) {
    when (target) {
        is NavigationTarget.BackToListDetails -> backToListDetails()
    }
}
