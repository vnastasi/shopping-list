package md.vnastasi.shoppinglist.screen.listdetails.nav

interface ListDetailsScreenNavigator {

    fun backToOverview()

    fun toAddItems(shoppingListId: Long)
}
