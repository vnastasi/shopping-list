package md.vnastasi.shoppinglist.screen.nav

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import md.vnastasi.shoppinglist.screen.additems.AddItemsViewModel
import md.vnastasi.shoppinglist.screen.additems.ui.AddItemsScreen
import md.vnastasi.shoppinglist.screen.listdetails.ListDetailsViewModel
import md.vnastasi.shoppinglist.screen.listdetails.ui.ListDetailsScreen
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
            ListOverviewScreen(
                navController = navController,
                viewModel = viewModel(
                    factory = koinInject<ListOverviewViewModel.Factory>()
                )
            )
        }

        composable(
            route = Routes.ListDetails.path,
            arguments = Routes.ListDetails.arguments
        ) { backStackEntry ->
            val shoppingListId = Routes.ListDetails.extractShoppingListId(backStackEntry.arguments)
            ListDetailsScreen(
                navController = navController,
                viewModel = viewModel(
                    factory = koinInject<ListDetailsViewModel.Factory>(),
                    extraArguments = bundleOf(ListDetailsViewModel.ARG_KEY_SHOPPING_LIST_ID to shoppingListId)
                )
            )
        }

        composable(
            route = Routes.AddItems.path,
            arguments = Routes.AddItems.arguments
        ) { backStackEntry ->
            val shoppingListId = Routes.AddItems.extractShoppingListId(backStackEntry.arguments)
            AddItemsScreen(
                navController = navController,
                viewModel = viewModel(
                    factory = koinInject<AddItemsViewModel.Factory>(),
                    extraArguments = bundleOf(AddItemsViewModel.ARG_KEY_SHOPPING_LIST_ID to shoppingListId)
                )
            )
        }
    }
}
