package md.vnastasi.shoppinglist.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import md.vnastasi.shoppinglist.screen.additems.ui.AddItemsScreen
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModel
import md.vnastasi.shoppinglist.screen.listdetails.ui.ListDetailsScreen
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModel
import md.vnastasi.shoppinglist.screen.overview.ui.OverviewScreen
import md.vnastasi.shoppinglist.screen.overview.vm.OverviewViewModel

fun NavGraphBuilder.overview(
    navController: NavController
) = composable<Routes.Overview>(
    exitTransition = { slideOutToLeft() },
    popEnterTransition = { slideInFromRight() },
    popExitTransition = { slideOutToRight() }
) { backStackEntry ->
    OverviewScreen(
        viewModel = hiltViewModel<OverviewViewModel>(backStackEntry),
        navigator = ScreenNavigators.overview(navController)
    )
}

fun NavGraphBuilder.listDetails(
    navController: NavController
) = composable<Routes.ListDetails>(
    enterTransition = { slideInFromLeft() },
    exitTransition = { slideOutToLeft() },
    popEnterTransition = { slideInFromRight() },
    popExitTransition = { slideOutToRight() }
) { backStackEntry ->
    ListDetailsScreen(
        viewModel = hiltViewModel<ListDetailsViewModel>(backStackEntry),
        navigator = ScreenNavigators.listDetails(navController)
    )
}

fun NavGraphBuilder.addItems(
    navController: NavController
) = composable<Routes.AddItems>(
    enterTransition = { slideInFromLeft() },
    exitTransition = { slideOutToLeft() },
    popEnterTransition = { slideInFromRight() },
    popExitTransition = { slideOutToRight() }
) { backStackEntry ->
    AddItemsScreen(
        viewModel = hiltViewModel<AddItemsViewModel>(backStackEntry),
        navigator = ScreenNavigators.addItems(navController)
    )
}

private fun AnimatedContentTransitionScope<*>.slideInFromLeft() =
    slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left)

private fun AnimatedContentTransitionScope<*>.slideInFromRight() =
    slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right)

private fun AnimatedContentTransitionScope<*>.slideOutToLeft() =
    slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left)

private fun AnimatedContentTransitionScope<*>.slideOutToRight() =
    slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right)
