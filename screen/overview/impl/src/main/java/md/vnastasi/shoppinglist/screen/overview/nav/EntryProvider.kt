package md.vnastasi.shoppinglist.screen.overview.nav

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.result.ResultEffect
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import md.vnastasi.shoppinglist.screen.listdetails.nav.ListDetails
import md.vnastasi.shoppinglist.screen.managelist.nav.BottomSheetClosedSignal
import md.vnastasi.shoppinglist.screen.managelist.nav.ManageList
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.overview.model.SwipeToRevealState
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.ui.OverviewScreen
import md.vnastasi.shoppinglist.screen.overview.vm.OverviewViewModel
import md.vnastasi.shoppinglist.screen.shared.nav.scene.ListDetailSceneStrategy
import md.vnastasi.shoppinglist.screen.shared.transition.ExtendedSharedTransitionScope

@Composable
context(sharedTransitionScope: SharedTransitionScope)
fun EntryProviderScope<NavKey>.Overview(navBackStack: NavBackStack<NavKey>) {
    entry<Overview>(
        metadata = ListDetailSceneStrategy.listPane()
    ) {
        val viewModel = hiltViewModel<OverviewViewModel>()

        val navigationCallback = remember<(NavigationTarget) -> Unit> {
            { target ->
                when (target) {
                    is NavigationTarget.ListDetails -> {
                        navBackStack.removeIf { it is ListDetails }
                        navBackStack.add(ListDetails(target.shoppingListId))
                    }

                    is NavigationTarget.AddOrEditList -> {
                        navBackStack.add(ManageList(target.shoppingListId))
                    }
                }
            }
        }

        ResultEffect<BottomSheetClosedSignal> { result ->
            val affectedShoppingListId = result.affectedShoppingListId
            if (affectedShoppingListId != null) {
                viewModel.dispatch(UiEvent.OnSwipeToRevealStateChanged(affectedShoppingListId, SwipeToRevealState.Content))
            }
        }

        context(ExtendedSharedTransitionScope(sharedTransitionScope = sharedTransitionScope, animatedContentScope = LocalNavAnimatedContentScope.current)) {
            OverviewScreen(
                viewModel = viewModel,
                onNavigate = navigationCallback
            )
        }
    }
}
