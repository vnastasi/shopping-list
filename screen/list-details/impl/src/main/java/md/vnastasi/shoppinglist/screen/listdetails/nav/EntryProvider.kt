package md.vnastasi.shoppinglist.screen.listdetails.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import md.vnastasi.shoppinglist.screen.additems.nav.AddItems
import md.vnastasi.shoppinglist.screen.listdetails.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.listdetails.ui.ListDetailsScreen
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModel
import md.vnastasi.shoppinglist.screen.shared.nav.scene.ListDetailSceneStrategy

@Composable
fun EntryProviderScope<NavKey>.ListDetails(navBackStack: NavBackStack<NavKey>) {
    entry<ListDetails>(
        metadata = ListDetailSceneStrategy.detailPane()
    ) { key ->
        val viewModel = hiltViewModel<ListDetailsViewModel, ListDetailsViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(shoppingListId = key.shoppingListId)
            }
        )

        val navigationCallback = remember<(NavigationTarget) -> Unit> {
            { target ->
                when (target) {
                    is NavigationTarget.BackToOverview -> {
                        navBackStack.removeLastOrNull()
                    }

                    is NavigationTarget.AddItems -> {
                        navBackStack.add(AddItems(target.shoppingListId))
                    }
                }
            }
        }

        ListDetailsScreen(
            viewModel = viewModel,
            onNavigate = navigationCallback
        )
    }
}
