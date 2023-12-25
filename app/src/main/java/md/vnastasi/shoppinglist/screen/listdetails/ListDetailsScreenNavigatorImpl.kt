package md.vnastasi.shoppinglist.screen.listdetails

import androidx.navigation.NavController
import md.vnastasi.shoppinglist.screen.nav.Routes

class ListDetailsScreenNavigatorImpl(
    private val navController: NavController
) : ListDetailsScreenNavigator {

    override fun backToOverview() {
        navController.navigateUp()
    }

    override fun toAddItems(shoppingListId: Long) {
        navController.navigate(Routes.AddItems(shoppingListId))
    }
}
