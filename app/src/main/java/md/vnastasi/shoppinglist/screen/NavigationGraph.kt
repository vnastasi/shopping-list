package md.vnastasi.shoppinglist.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import md.vnastasi.shoppinglist.screen.main.MainScreen
import md.vnastasi.shoppinglist.screen.main.MainViewModel
import md.vnastasi.shoppinglist.screen.shoppinglist.ShoppingListScreen
import org.koin.androidx.compose.get

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {

        composable(
            route = "main"
        ) {
            MainScreen(navController, viewModel(factory = get<MainViewModel.Factory>()))
        }

        composable(
            route = "shopping-list/{shoppingListId}",
            arguments = listOf(
                navArgument("shoppingListId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val shoppingListId = requireNotNull(backStackEntry.arguments?.getLong("shoppingListId"))
            ShoppingListScreen(shoppingListId)
        }
    }
}
