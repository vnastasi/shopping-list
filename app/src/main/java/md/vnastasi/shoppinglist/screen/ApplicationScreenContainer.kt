package md.vnastasi.shoppinglist.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun ApplicationScreenContainer() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Overview
    ) {

        overview(
            navController = navController
        )

        listDetails(
            navController = navController
        )

        addItems(
            navController = navController
        )
    }
}
