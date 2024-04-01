package md.vnastasi.shoppinglist.screen.listdetails.ui

import md.vnastasi.shoppinglist.screen.listdetails.nav.ListDetailsScreenNavigator

class StubListDetailsScreenNavigator : ListDetailsScreenNavigator {

    override fun backToOverview() = Unit

    override fun toAddItems(shoppingListId: Long) = Unit
}
