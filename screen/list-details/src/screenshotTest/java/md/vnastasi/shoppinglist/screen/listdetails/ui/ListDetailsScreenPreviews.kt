package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingItem
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.support.collection.ScreenshotPreviews
import md.vnastasi.shoppinglist.support.theme.AppTheme

@PreviewTest
@ScreenshotPreviews
@Composable
fun EmptyListDetailsScreen() {
    val viewState = ViewState.Ready(
        shoppingListId = 1L,
        shoppingListName = "Test list",
        listOfShoppingItems = persistentListOf(),
        navigationTarget = null
    )
    AppTheme {
        ListDetailsScreen(
            viewModel = StubListDetailsViewModelSpec(viewState),
            navigator = StubListDetailsScreenNavigator()
        )
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
fun ReadyListDetailsScreen() {
    val viewState = ViewState.Ready(
        shoppingListId = 1L,
        shoppingListName = "Test list",
        listOfShoppingItems = persistentListOf(
            createShoppingItem {
                id = 1L
                name = "Item 1"
                isChecked = false
            },
            createShoppingItem {
                id = 2L
                name = "Item 2"
                isChecked = true
            }
        ),
        navigationTarget = null
    )
    AppTheme {
        ListDetailsScreen(
            viewModel = StubListDetailsViewModelSpec(viewState),
            navigator = StubListDetailsScreenNavigator()
        )
    }
}
