package md.vnastasi.shoppinglist.screen.additems.nav

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import md.vnastasi.shoppinglist.screen.additems.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.additems.ui.AddItemsScreen
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModel
import md.vnastasi.shoppinglist.screen.shared.animation.slideInFromDown
import md.vnastasi.shoppinglist.screen.shared.animation.slideOutToDown
import md.vnastasi.shoppinglist.screen.shared.nav.scene.DialogWhenLargeSceneStrategy

@Composable
fun EntryProviderScope<NavKey>.AddItems(navBackStack: NavBackStack<NavKey>) {
    entry<AddItems>(
        metadata = DialogWhenLargeSceneStrategy.dialogWhenLarge() +
                NavDisplay.transitionSpec { slideInFromDown() togetherWith ExitTransition.KeepUntilTransitionsFinished } +
                NavDisplay.popTransitionSpec { EnterTransition.None togetherWith slideOutToDown() } +
                NavDisplay.predictivePopTransitionSpec { EnterTransition.None togetherWith slideOutToDown() }
    ) { key ->
        val viewModel = hiltViewModel<AddItemsViewModel, AddItemsViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(key.shoppingListId)
            }
        )

        val navigationCallback = remember<(NavigationTarget) -> Unit> {
            { target ->
                when (target) {
                    is NavigationTarget.BackToListDetails -> {
                        navBackStack.removeLastOrNull()
                    }
                }
            }
        }

        AddItemsScreen(
            viewModel = viewModel,
            onNavigate = navigationCallback
        )
    }
}
