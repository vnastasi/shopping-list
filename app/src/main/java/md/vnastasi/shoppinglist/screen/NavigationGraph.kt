package md.vnastasi.shoppinglist.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import md.vnastasi.shoppinglist.screen.main.MainScreen
import md.vnastasi.shoppinglist.screen.shoppinglist.ShoppingListScreen

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {

        composable(
            route = "main"
        ) {
            MainScreen(navController)
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
