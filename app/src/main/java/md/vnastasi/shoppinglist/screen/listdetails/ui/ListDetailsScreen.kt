package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import md.vnastasi.shoppinglist.screen.listdetails.ListDetailsViewModel
import md.vnastasi.shoppinglist.screen.listdetails.UiEvent
import md.vnastasi.shoppinglist.support.state.ScreenState

@Composable
fun ListDetailsScreen(
    navController: NavController,
    viewModel: ListDetailsViewModel
) {
    when (val screenState = viewModel.screenState.collectAsState().value) {
        is ScreenState.Ready -> ListDetailsContent(
            screenState = screenState,
            onNavigateBack = { navController.navigateUp() },
            onListItemClicked = { viewModel.onUiEvent(UiEvent.OnShoppingListItemClicked(it)) }
        )

        else -> Unit
    }
}
