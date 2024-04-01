package md.vnastasi.shoppinglist.nav

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModel
import md.vnastasi.shoppinglist.screen.additems.ui.AddItemsScreen
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModel
import md.vnastasi.shoppinglist.screen.listdetails.ui.ListDetailsScreen
import md.vnastasi.shoppinglist.screen.overview.vm.ListOverviewViewModel
import md.vnastasi.shoppinglist.screen.overview.ui.ListOverviewScreen
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
            val viewModel = viewModel<ListOverviewViewModel>(
                factory = koinInject<ListOverviewViewModel.Factory>()
            )
            ListOverviewScreen(
                viewModel = viewModel,
                navigator = ScreenNavigators.overview(navController)
            )
        }

        composable(
            route = Routes.ListDetails.path,
            arguments = Routes.ListDetails.arguments
        ) { backStackEntry ->
            val shoppingListId = Routes.ListDetails.extractShoppingListId(backStackEntry.arguments)
            val viewModel = viewModel<ListDetailsViewModel>(
                factory = koinInject<ListDetailsViewModel.Factory>(),
                extraArguments = bundleOf(ListDetailsViewModel.ARG_KEY_SHOPPING_LIST_ID to shoppingListId)
            )
            ListDetailsScreen(
                viewModel = viewModel,
                navigator = ScreenNavigators.listDetails(navController)
            )
        }

        composable(
            route = Routes.AddItems.path,
            arguments = Routes.AddItems.arguments
        ) { backStackEntry ->
            val shoppingListId = Routes.AddItems.extractShoppingListId(backStackEntry.arguments)
            val viewModel = viewModel<AddItemsViewModel>(
                factory = koinInject<AddItemsViewModel.Factory>(),
                extraArguments = bundleOf(AddItemsViewModel.ARG_KEY_SHOPPING_LIST_ID to shoppingListId)
            )
            AddItemsScreen(
                viewModel = viewModel,
                navigator = ScreenNavigators.addItems(navController)
            )
        }
    }
}
