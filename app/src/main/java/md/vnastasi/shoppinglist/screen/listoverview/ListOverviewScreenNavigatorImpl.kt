package md.vnastasi.shoppinglist.screen.listoverview

import androidx.navigation.NavController
import md.vnastasi.shoppinglist.screen.nav.Routes

class ListOverviewScreenNavigatorImpl(
    private val navController: NavController
) : ListOverviewScreenNavigator {

    override fun toListDetails(shoppingListId: Long) {
        navController.navigate(Routes.ListDetails(shoppingListId))
    }
}
