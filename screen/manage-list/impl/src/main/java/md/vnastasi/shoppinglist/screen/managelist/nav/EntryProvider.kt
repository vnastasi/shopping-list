package md.vnastasi.shoppinglist.screen.managelist.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.result.LocalResultEventBus
import md.vnastasi.shoppinglist.screen.managelist.model.BottomSheetClosedSignal
import md.vnastasi.shoppinglist.screen.managelist.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.managelist.ui.ManageListSheet
import md.vnastasi.shoppinglist.screen.managelist.vm.ManageListViewModel
import md.vnastasi.shoppinglist.screen.shared.nav.scene.BottomSheetSceneStrategy

@Composable
fun EntryProviderScope<NavKey>.ManageList(navBackStack: NavBackStack<NavKey>) {
    entry<ManageList>(
        metadata = BottomSheetSceneStrategy.bottomSheet()
    ) { key ->

        val viewModel = hiltViewModel<ManageListViewModel, ManageListViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(key.shoppingListId)
            }
        )

        val eventBus = LocalResultEventBus.current
        val navigationCallback = remember<(NavigationTarget) -> Unit> {
            { target ->
                when (target) {
                    is NavigationTarget.CloseSheet -> {
                        navBackStack.removeLastOrNull()
                        eventBus.sendResult(BottomSheetClosedSignal(target.affectedShoppingListId))
                    }
                }
            }
        }

        ManageListSheet(
            viewModel = viewModel,
            onNavigate = navigationCallback
        )
    }
}
