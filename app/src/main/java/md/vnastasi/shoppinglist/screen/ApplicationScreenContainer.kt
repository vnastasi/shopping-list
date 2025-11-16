package md.vnastasi.shoppinglist.screen

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import md.vnastasi.shoppinglist.screen.additems.ui.AddItemsScreen
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModel
import md.vnastasi.shoppinglist.screen.listdetails.ui.ListDetailsScreen
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModel
import md.vnastasi.shoppinglist.screen.overview.ui.OverviewScreen
import md.vnastasi.shoppinglist.screen.overview.vm.OverviewViewModel

@Composable
fun ApplicationScreenContainer() {

    val navBackStack = rememberNavBackStack(Routes.Overview)

    NavDisplay(
        backStack = navBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = {
            navBackStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry<Routes.Overview> {
                OverviewScreen(
                    viewModel = hiltViewModel<OverviewViewModel>(),
                    navigator = TODO()
                )
            }

            entry<Routes.ListDetails> { key ->
                ListDetailsScreen(
                    viewModel = hiltViewModel<ListDetailsViewModel, ListDetailsViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key.shoppingListId)
                        }
                    ),
                    navigator = TODO()
                )
            }

            entry<Routes.AddItems> { key ->
                AddItemsScreen(
                    viewModel = hiltViewModel<AddItemsViewModel, AddItemsViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key.shoppingListId)
                        }
                    ),
                    navigator = TODO()
                )
            }
        }
    )
}
