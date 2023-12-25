package md.vnastasi.shoppinglist.screen.nav

import androidx.navigation.NavController
import md.vnastasi.shoppinglist.screen.additems.AddItemsScreenNavigator

class AddItemsScreenNavigatorImpl(
    private val navController: NavController
) : AddItemsScreenNavigator {

    override fun backToListDetails() {
        navController.navigateUp()
    }
}
