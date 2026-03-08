package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.support.collection.ScreenshotPreviews
import md.vnastasi.shoppinglist.support.theme.AppTheme

@PreviewTest
@ScreenshotPreviews
@Composable
fun LoadingOverviewScreen() {
    val viewState = ViewState.Loading

    AppTheme {
        OverviewScreen(
            viewModel = StubOverviewViewModel(viewState),
            navigator = StubOverviewScreenNavigator()
        )
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
fun EmptyOverviewScreen() {
    val viewState = ViewState.Empty(
        navigationTarget = null
    )

    AppTheme {
        OverviewScreen(
            viewModel = StubOverviewViewModel(viewState),
            navigator = StubOverviewScreenNavigator()
        )
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
fun ReadyOverviewScreen() {
    val viewState = ViewState.Ready(
        data = persistentListOf(
            ShoppingListDetails(id = 1L, name = "Groceries", position = 1L, totalItems = 10L, checkedItems = 2L),
            ShoppingListDetails(id = 2L, name = "Pharmacy for mom", position = 2L, totalItems = 1L, checkedItems = 1L),
            ShoppingListDetails(id = 3L, name = "Gamma & Praxis", position = 3L, totalItems = 4L, checkedItems = 0L),
            ShoppingListDetails(id = 4L, name = "Birthday party shopping list", position = 4L, totalItems = 3L, checkedItems = 3L),
            ShoppingListDetails(id = 5L, name = "Christmas Eve party", position = 5L, totalItems = 0L, checkedItems = 0L),
            ShoppingListDetails(id = 6L, name = "Thanksgiving family reunion", position = 6L, totalItems = 23L, checkedItems = 5L),
        ),
        navigationTarget = null
    )

    AppTheme {
        OverviewScreen(
            viewModel = StubOverviewViewModel(viewState),
            navigator = StubOverviewScreenNavigator()
        )
    }
}
