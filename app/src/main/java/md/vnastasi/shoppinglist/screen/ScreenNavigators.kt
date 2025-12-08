package md.vnastasi.shoppinglist.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import md.vnastasi.shoppinglist.screen.additems.nav.AddItemsScreenNavigator
import md.vnastasi.shoppinglist.screen.listdetails.nav.ListDetailsScreenNavigator
import md.vnastasi.shoppinglist.screen.managelist.nav.ManageListNavigator
import md.vnastasi.shoppinglist.screen.overview.nav.OverviewScreenNavigator

object ScreenNavigators {

    @Composable
    fun overview(navBackStack: NavBackStack<NavKey>) = remember<OverviewScreenNavigator> { OverviewScreenNavigatorImpl(navBackStack) }

    @Composable
    fun manageList(navBackStack: NavBackStack<NavKey>) = remember<ManageListNavigator> { ManageListNavigatorImpl(navBackStack) }

    @Composable
    fun listDetails(navBackStack: NavBackStack<NavKey>) = remember<ListDetailsScreenNavigator> { ListDetailsScreenNavigatorImpl(navBackStack) }

    @Composable
    fun addItems(navBackStack: NavBackStack<NavKey>) = remember<AddItemsScreenNavigator> { AddItemsScreenNavigatorImpl(navBackStack) }
}

private class OverviewScreenNavigatorImpl(
    private val navBackStack: NavBackStack<NavKey>
) : OverviewScreenNavigator {

    override fun toListDetails(shoppingListId: Long) {
        navBackStack.add(Routes.ListDetails(shoppingListId))
    }

    override fun openManageListSheet(shoppingListId: Long?) {
        navBackStack.add(Routes.ManageList(shoppingListId))
    }
}

private class ManageListNavigatorImpl(
    private val navBackStack: NavBackStack<NavKey>
) : ManageListNavigator {

    override fun close() {
        navBackStack.removeLastOrNull()
    }
}

private class ListDetailsScreenNavigatorImpl(
    private val navBackStack: NavBackStack<NavKey>
) : ListDetailsScreenNavigator {

    override fun backToOverview() {
        navBackStack.removeLastOrNull()
    }

    override fun toAddItems(shoppingListId: Long) {
        navBackStack.add(Routes.AddItems(shoppingListId))
    }
}

private class AddItemsScreenNavigatorImpl(
    private val navBackStack: NavBackStack<NavKey>
) : AddItemsScreenNavigator {

    override fun backToListDetails() {
        navBackStack.removeLastOrNull()
    }
}
