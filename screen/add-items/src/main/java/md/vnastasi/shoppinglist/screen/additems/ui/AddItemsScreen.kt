package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    if (LocalPresentationMode.current == PresentationMode.Dialog) {
        AddItemsDialog(
            searchTermTextFieldState = viewModel.searchTermTextFieldState,
            viewState = viewModel.viewState.collectAsStateWithLifecycle(),
            onDone = navigator::backToListDetails,
            onItemAddedToList = { name -> viewModel.onUiEvent(UiEvent.ItemAddedToList(name)) },
            onSuggestionDeleted = { suggestion -> viewModel.onUiEvent(UiEvent.SuggestionDeleted(suggestion)) }
        )
    } else {
        AddItemsFullScreen(
            searchTermTextFieldState = viewModel.searchTermTextFieldState,
            viewState = viewModel.viewState.collectAsStateWithLifecycle(),
            onNavigateUp = navigator::backToListDetails,
            onItemAddedToList = { name -> viewModel.onUiEvent(UiEvent.ItemAddedToList(name)) },
            onSuggestionDeleted = { suggestion -> viewModel.onUiEvent(UiEvent.SuggestionDeleted(suggestion)) },
            onToastShown = { viewModel.onUiEvent(UiEvent.ToastShown) }
        )
    }
}
