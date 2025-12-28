package md.vnastasi.shoppinglist.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import md.vnastasi.shoppinglist.nav.Route
import md.vnastasi.shoppinglist.nav.ScreenNavigators
import md.vnastasi.shoppinglist.scene.BottomSheetSceneStrategy
import md.vnastasi.shoppinglist.scene.ListDetailSceneStrategy
import md.vnastasi.shoppinglist.scene.rememberBottomSheetSceneStrategy
import md.vnastasi.shoppinglist.scene.rememberListDetailSceneStrategy
import md.vnastasi.shoppinglist.screen.additems.ui.AddItemsScreen
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModel
import md.vnastasi.shoppinglist.screen.listdetails.ui.ListDetailsScreen
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModel
import md.vnastasi.shoppinglist.screen.managelist.ui.ManageListSheet
import md.vnastasi.shoppinglist.screen.managelist.vm.ManageListViewModel
import md.vnastasi.shoppinglist.screen.overview.ui.OverviewScreen
import md.vnastasi.shoppinglist.screen.overview.vm.OverviewViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ApplicationScreenContainer() {

    val navBackStack = rememberNavBackStack(Route.Overview)

    NavDisplay(
        backStack = navBackStack,
        sceneStrategy = rememberListDetailSceneStrategy<NavKey>().then(rememberBottomSheetSceneStrategy()),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            slideInFromRight() togetherWith slideOutToLeft()
        },
        popTransitionSpec = {
            slideInFromLeft() togetherWith slideOutToRight()
        },
        predictivePopTransitionSpec = {
            slideInFromLeft() togetherWith slideOutToRight()
        },
        onBack = {
            navBackStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry<Route.Overview>(
                metadata = ListDetailSceneStrategy.listPane()
            ) {
                OverviewScreen(
                    viewModel = hiltViewModel<OverviewViewModel>(),
                    navigator = ScreenNavigators.overview(navBackStack)
                )
            }

            entry<Route.ManageList>(
                metadata = BottomSheetSceneStrategy.bottomSheet()
            ) { key ->
                ManageListSheet(
                    viewModel = hiltViewModel<ManageListViewModel, ManageListViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key.shoppingListId)
                        }
                    ),
                    navigator = ScreenNavigators.manageList(navBackStack)
                )
            }

            entry<Route.ListDetails>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { key ->
                ListDetailsScreen(
                    viewModel = hiltViewModel<ListDetailsViewModel, ListDetailsViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(
                                shoppingListId = key.shoppingListId
                            )
                        }
                    ),
                    navigator = ScreenNavigators.listDetails(navBackStack)
                )
            }

            entry<Route.AddItems>(
                metadata = NavDisplay.transitionSpec {
                    slideInFromDown() togetherWith ExitTransition.KeepUntilTransitionsFinished
                } + NavDisplay.popTransitionSpec {
                    EnterTransition.None togetherWith slideOutToDown()
                } + NavDisplay.predictivePopTransitionSpec {
                    EnterTransition.None togetherWith slideOutToDown()
                }
            ) { key ->
                AddItemsScreen(
                    viewModel = hiltViewModel<AddItemsViewModel, AddItemsViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key.shoppingListId)
                        }
                    ),
                    navigator = ScreenNavigators.addItems(navBackStack)
                )
            }
        }
    )
}
