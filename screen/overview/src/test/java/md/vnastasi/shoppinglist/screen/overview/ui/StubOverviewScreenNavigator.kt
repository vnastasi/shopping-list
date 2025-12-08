package md.vnastasi.shoppinglist.screen.overview.ui

import md.vnastasi.shoppinglist.screen.overview.nav.OverviewScreenNavigator

class StubOverviewScreenNavigator : OverviewScreenNavigator {

    override fun toListDetails(shoppingListId: Long) = Unit

    override fun openManageListSheet(shoppingListId: Long?) = Unit
}
