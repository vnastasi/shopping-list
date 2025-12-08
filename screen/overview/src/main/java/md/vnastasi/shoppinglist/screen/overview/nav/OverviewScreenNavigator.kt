package md.vnastasi.shoppinglist.screen.overview.nav

interface OverviewScreenNavigator {

    fun toListDetails(shoppingListId: Long)

    fun openManageListSheet(shoppingListId: Long?)
}
