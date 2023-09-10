package md.vnastasi.shoppinglist.screen.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import md.vnastasi.shoppinglist.screen.listdetails.ListDetailsScreen
import md.vnastasi.shoppinglist.screen.listoverview.ListOverviewScreen
import md.vnastasi.shoppinglist.screen.listoverview.ListOverviewViewModel
import org.koin.compose.koinInject

@Composable
fun NavigationGraph() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "shopping-list") {

        composable(
            route = "shopping-list"
        ) {
            ListOverviewScreen(
                navController = navController,
                viewModel = viewModel(
                    factory = koinInject<ListOverviewViewModel.Factory>()
                )
            )
        }

        composable(
            route = "shopping-list/{shoppingListId}",
            arguments = listOf(
                navArgument("shoppingListId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val shoppingListId = requireNotNull(backStackEntry.arguments?.getLong("shoppingListId"))
            ListDetailsScreen(shoppingListId)
        }
    }
}
