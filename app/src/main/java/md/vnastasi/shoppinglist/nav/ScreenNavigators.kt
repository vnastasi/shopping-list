package md.vnastasi.shoppinglist.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.result.LocalResultEventBus
import androidx.navigation3.runtime.result.ResultEventBus
import md.vnastasi.shoppinglist.screen.additems.nav.AddItemsScreenNavigator
import md.vnastasi.shoppinglist.screen.listdetails.nav.ListDetailsScreenNavigator
import md.vnastasi.shoppinglist.screen.managelist.model.BottomSheetClosedSignal
import md.vnastasi.shoppinglist.screen.managelist.nav.ManageListNavigator
import md.vnastasi.shoppinglist.screen.overview.nav.OverviewScreenNavigator

internal object ScreenNavigators {

    @Composable
    fun overview(navBackStack: NavBackStack<NavKey>) =
        remember<OverviewScreenNavigator> { OverviewScreenNavigatorImpl(navBackStack) }

    @Composable
    fun manageList(navBackStack: NavBackStack<NavKey>): ManageListNavigator {
        val eventBus = LocalResultEventBus.current
        return remember<ManageListNavigator> { ManageListNavigatorImpl(navBackStack, eventBus) }
    }

    @Composable
    fun listDetails(navBackStack: NavBackStack<NavKey>) =
        remember<ListDetailsScreenNavigator> { ListDetailsScreenNavigatorImpl(navBackStack) }

    @Composable
    fun addItems(navBackStack: NavBackStack<NavKey>) =
        remember<AddItemsScreenNavigator> { AddItemsScreenNavigatorImpl(navBackStack) }
}

private class OverviewScreenNavigatorImpl(
    private val navBackStack: NavBackStack<NavKey>
) : OverviewScreenNavigator {

    override fun toListDetails(shoppingListId: Long) {
        navBackStack.removeIf { it is Route.ListDetails }
        navBackStack.add(Route.ListDetails(shoppingListId))
    }

    override fun openManageListSheet(shoppingListId: Long?) {
        navBackStack.add(Route.ManageList(shoppingListId))
    }
}

private class ManageListNavigatorImpl(
    private val navBackStack: NavBackStack<NavKey>,
    private val eventBus: ResultEventBus
) : ManageListNavigator {

    override fun closeSheet(affectedShoppingListId: Long?) {
        navBackStack.removeLastOrNull()
        eventBus.sendResult(BottomSheetClosedSignal(affectedShoppingListId))
    }
}

private class ListDetailsScreenNavigatorImpl(
    private val navBackStack: NavBackStack<NavKey>
) : ListDetailsScreenNavigator {

    override fun backToOverview() {
        navBackStack.removeLastOrNull()
    }

    override fun toAddItems(shoppingListId: Long) {
        navBackStack.add(Route.AddItems(shoppingListId))
    }
}

private class AddItemsScreenNavigatorImpl(
    private val navBackStack: NavBackStack<NavKey>
) : AddItemsScreenNavigator {

    override fun backToListDetails() {
        navBackStack.removeLastOrNull()
    }
}
