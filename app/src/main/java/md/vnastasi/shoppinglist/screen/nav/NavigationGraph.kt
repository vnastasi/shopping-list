package md.vnastasi.shoppinglist.screen.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.os.bundleOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import md.vnastasi.shoppinglist.screen.additems.AddItemsScreenNavigator
import md.vnastasi.shoppinglist.screen.additems.AddItemsScreenNavigatorImpl
import md.vnastasi.shoppinglist.screen.additems.AddItemsViewModel
import md.vnastasi.shoppinglist.screen.additems.ui.AddItemsScreen
import md.vnastasi.shoppinglist.screen.listdetails.ListDetailsScreenNavigator
import md.vnastasi.shoppinglist.screen.listdetails.ListDetailsScreenNavigatorImpl
import md.vnastasi.shoppinglist.screen.listdetails.ListDetailsViewModel
import md.vnastasi.shoppinglist.screen.listdetails.ui.ListDetailsScreen
import md.vnastasi.shoppinglist.screen.listoverview.ListOverviewScreenNavigator
import md.vnastasi.shoppinglist.screen.listoverview.ListOverviewScreenNavigatorImpl
import md.vnastasi.shoppinglist.screen.listoverview.ListOverviewViewModel
import md.vnastasi.shoppinglist.screen.listoverview.ui.ListOverviewScreen
import md.vnastasi.shoppinglist.support.lifecycle.viewModel
import org.koin.compose.koinInject

@Composable
fun NavigationGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.ListOverview()
    ) {

        composable(
            route = Routes.ListOverview.path
        ) {
            val navigator = remember<ListOverviewScreenNavigator> { ListOverviewScreenNavigatorImpl(navController) }
            ListOverviewScreen(
                viewModel = viewModel(
                    factory = koinInject<ListOverviewViewModel.Factory>()
                ),
                navigator = navigator
            )
        }

        composable(
            route = Routes.ListDetails.path,
            arguments = Routes.ListDetails.arguments
        ) { backStackEntry ->
            val shoppingListId = Routes.ListDetails.extractShoppingListId(backStackEntry.arguments)
            val navigator = remember<ListDetailsScreenNavigator> { ListDetailsScreenNavigatorImpl(navController) }
            ListDetailsScreen(
                viewModel = viewModel(
                    factory = koinInject<ListDetailsViewModel.Factory>(),
                    extraArguments = bundleOf(ListDetailsViewModel.ARG_KEY_SHOPPING_LIST_ID to shoppingListId)
                ),
                navigator = navigator
            )
        }

        composable(
            route = Routes.AddItems.path,
            arguments = Routes.AddItems.arguments
        ) { backStackEntry ->
            val shoppingListId = Routes.AddItems.extractShoppingListId(backStackEntry.arguments)
            val navigator = remember<AddItemsScreenNavigator> { AddItemsScreenNavigatorImpl(navController) }
            AddItemsScreen(
                viewModel = viewModel(
                    factory = koinInject<AddItemsViewModel.Factory>(),
                    extraArguments = bundleOf(AddItemsViewModel.ARG_KEY_SHOPPING_LIST_ID to shoppingListId)
                ),
                navigator = navigator
            )
        }
    }
}
