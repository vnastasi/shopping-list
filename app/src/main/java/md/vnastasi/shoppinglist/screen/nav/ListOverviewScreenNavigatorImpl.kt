package md.vnastasi.shoppinglist.screen.nav

import androidx.navigation.NavController
import md.vnastasi.shoppinglist.screen.overview.ListOverviewScreenNavigator

class ListOverviewScreenNavigatorImpl(
    private val navController: NavController
) : ListOverviewScreenNavigator {

    override fun toListDetails(shoppingListId: Long) {
        navController.navigate(Routes.ListDetails(shoppingListId))
    }
}
