package md.vnastasi.shoppinglist.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import md.vnastasi.shoppinglist.screen.additems.AddItemsScreenNavigator
import md.vnastasi.shoppinglist.screen.listdetails.nav.ListDetailsScreenNavigator
import md.vnastasi.shoppinglist.screen.overview.nav.ListOverviewScreenNavigator

object ScreenNavigators {

    @Composable
    fun overview(navController: NavController) = remember<ListOverviewScreenNavigator> { ListOverviewScreenNavigatorImpl(navController) }

    @Composable
    fun listDetails(navController: NavController) = remember<ListDetailsScreenNavigator> { ListDetailsScreenNavigatorImpl(navController) }

    @Composable
    fun addItems(navController: NavController) = remember<AddItemsScreenNavigator> { AddItemsScreenNavigatorImpl(navController) }
}

private class ListOverviewScreenNavigatorImpl(
    private val navController: NavController
) : ListOverviewScreenNavigator {

    override fun toListDetails(shoppingListId: Long) {
        navController.navigate(Routes.ListDetails(shoppingListId))
    }
}

private class ListDetailsScreenNavigatorImpl(
    private val navController: NavController
) : ListDetailsScreenNavigator {

    override fun backToOverview() {
        navController.navigateUp()
    }

    override fun toAddItems(shoppingListId: Long) {
        navController.navigate(Routes.AddItems(shoppingListId))
    }
}

private class AddItemsScreenNavigatorImpl(
    private val navController: NavController
) : AddItemsScreenNavigator {

    override fun backToListDetails() {
        navController.navigateUp()
    }
}
