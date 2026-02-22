package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import md.vnastasi.shoppinglist.screen.additems.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.nav.AddItemsScreenNavigator
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModelSpec
import md.vnastasi.shoppinglist.screen.shared.content.LocalPresentationMode
import md.vnastasi.shoppinglist.screen.shared.content.PresentationMode

@Composable
fun AddItemsScreen(
    viewModel: AddItemsViewModelSpec,
    navigator: AddItemsScreenNavigator
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(viewState.navigationTarget) {
        when (viewState.navigationTarget) {
            is NavigationTarget.BackToListDetails -> {
                navigator.backToListDetails()
                viewModel.dispatch(UiEvent.OnNavigationConsumed)
            }

            null -> Unit
        }
    }

    if (LocalPresentationMode.current == PresentationMode.Dialog) {
        AddItemsDialog(
            searchTermTextFieldState = viewModel.searchTermTextFieldState,
            viewState = viewState,
            dispatchEvent = viewModel::dispatch,
        )
    } else {
        AddItemsFullScreen(
            searchTermTextFieldState = viewModel.searchTermTextFieldState,
            viewState = viewState,
            dispatchEvent = viewModel::dispatch,
        )
    }
}
