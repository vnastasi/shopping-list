package md.vnastasi.shoppinglist.screen.additems

import androidx.navigation.NavController

class AddItemsScreenNavigatorImpl(
    private val navController: NavController
) : AddItemsScreenNavigator {

    override fun backToListDetails() {
        navController.navigateUp()
    }
}
