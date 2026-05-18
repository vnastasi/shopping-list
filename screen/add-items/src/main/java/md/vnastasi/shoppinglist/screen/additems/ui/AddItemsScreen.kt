package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.screen.additems.model.Effect
import md.vnastasi.shoppinglist.screen.additems.nav.AddItemsScreenNavigator
import md.vnastasi.shoppinglist.screen.additems.nav.navigate
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModelSpec
import md.vnastasi.shoppinglist.screen.shared.content.LocalPresentationMode
import md.vnastasi.shoppinglist.screen.shared.content.PresentationMode

@Composable
fun AddItemsScreen(
    viewModel: AddItemsViewModelSpec,
    navigator: AddItemsScreenNavigator
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LifecycleStartEffect(viewModel.effect) {
        val job = lifecycleScope.launch {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is Effect.Navigation -> navigator.navigate(effect.target)
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }

    when (LocalPresentationMode.current) {
        PresentationMode.Dialog -> {
            AddItemsDialog(
                searchTermTextFieldState = viewModel.searchTermTextFieldState,
                viewState = viewState,
                dispatchEvent = viewModel::dispatch,
            )
        }

        PresentationMode.FullScreen -> {
            AddItemsFullScreen(
                searchTermTextFieldState = viewModel.searchTermTextFieldState,
                viewState = viewState,
                dispatchEvent = viewModel::dispatch,
            )
        }
    }
}
