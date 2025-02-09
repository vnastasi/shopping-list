package md.vnastasi.shoppinglist.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import md.vnastasi.shoppinglist.support.di.ViewModelFactoryCreator

@Composable
fun ApplicationScreenContainer(
    viewModelFactories: ViewModelFactoryCreator
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Overview
    ) {

        overview(
            navController = navController,
            viewModelFactories = viewModelFactories
        )

        listDetails(
            navController = navController,
            viewModelFactories = viewModelFactories
        )

        addItems(
            navController = navController,
            viewModelFactories = viewModelFactories
        )
    }
}
