package md.vnastasi.shoppinglist.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import md.vnastasi.shoppinglist.screen.additems.nav.AddItemsScreenNavigator
import md.vnastasi.shoppinglist.screen.listdetails.nav.ListDetailsScreenNavigator
import md.vnastasi.shoppinglist.screen.overview.nav.OverviewScreenNavigator

object ScreenNavigators {

    @Composable
    fun overview(navController: NavController) = remember<OverviewScreenNavigator> { OverviewScreenNavigatorImpl(navController) }

    @Composable
    fun listDetails(navController: NavController) = remember<ListDetailsScreenNavigator> { ListDetailsScreenNavigatorImpl(navController) }

    @Composable
    fun addItems(navController: NavController) = remember<AddItemsScreenNavigator> { AddItemsScreenNavigatorImpl(navController) }
}

private class OverviewScreenNavigatorImpl(
    private val navController: NavController
) : OverviewScreenNavigator {

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
