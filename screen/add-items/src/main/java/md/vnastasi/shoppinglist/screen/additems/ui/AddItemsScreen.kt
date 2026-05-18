package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import md.vnastasi.shoppinglist.screen.shared.toast.Toast
import md.vnastasi.shoppinglist.screen.shared.toast.ToastMessage

@Composable
fun AddItemsScreen(
    viewModel: AddItemsViewModelSpec,
    navigator: AddItemsScreenNavigator
) {
    var toastMessage by remember { mutableStateOf<ToastMessage?>(null) }

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LifecycleStartEffect(viewModel.effect) {
        val job = lifecycleScope.launch {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is Effect.Navigation -> navigator.navigate(effect.target)
                    is Effect.ShowToast -> toastMessage = effect.toastMessage
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

            Toast(
                message = toastMessage
            )
        }
    }
}
