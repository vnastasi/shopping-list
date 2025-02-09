package md.vnastasi.shoppinglist.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import md.vnastasi.shoppinglist.support.di.ViewModelFactoryCreator
import md.vnastasi.shoppinglist.screen.additems.ui.AddItemsScreen
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModel
import md.vnastasi.shoppinglist.screen.listdetails.ui.ListDetailsScreen
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModel
import md.vnastasi.shoppinglist.screen.overview.ui.OverviewScreen
import md.vnastasi.shoppinglist.screen.overview.vm.OverviewViewModel
import md.vnastasi.shoppinglist.support.lifecycle.viewModel

@Composable
fun NavigationGraph(
    viewModelFactories: ViewModelFactoryCreator
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Overview
    ) {

        composable<Routes.Overview>(
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromRight() },
            popExitTransition = { slideOutToRight() }
        ) {
            val viewModel = viewModel<OverviewViewModel>(
                factory = viewModelFactories.create<OverviewViewModel.Factory>()
            )
            OverviewScreen(
                viewModel = viewModel,
                navigator = ScreenNavigators.overview(navController)
            )
        }

        composable<Routes.ListDetails>(
            enterTransition = { slideInFromLeft() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromRight() },
            popExitTransition = { slideOutToRight() }
        ) {
            val viewModel = viewModel<ListDetailsViewModel>(
                factory = viewModelFactories.create<ListDetailsViewModel.Factory>()
            )
            ListDetailsScreen(
                viewModel = viewModel,
                navigator = ScreenNavigators.listDetails(navController)
            )
        }

        composable<Routes.AddItems>(
            enterTransition = { slideInFromLeft() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromRight() },
            popExitTransition = { slideOutToRight() }
        ) {
            val viewModel = viewModel<AddItemsViewModel>(
                factory = viewModelFactories.create<AddItemsViewModel.Factory>(),
            )
            AddItemsScreen(
                viewModel = viewModel,
                navigator = ScreenNavigators.addItems(navController)
            )
        }
    }
}

private fun AnimatedContentTransitionScope<*>.slideInFromLeft() =
    slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left)

private fun AnimatedContentTransitionScope<*>.slideInFromRight() =
    slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right)

private fun AnimatedContentTransitionScope<*>.slideOutToLeft() =
    slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left)

private fun AnimatedContentTransitionScope<*>.slideOutToRight() =
    slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right)
