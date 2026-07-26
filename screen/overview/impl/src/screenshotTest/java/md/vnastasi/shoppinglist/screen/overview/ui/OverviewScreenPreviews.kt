package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.screen.overview.model.ShoppingListUiModel
import md.vnastasi.shoppinglist.screen.overview.model.SwipeToRevealState
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.transition.PreviewableSharedTransitionLayout
import md.vnastasi.shoppinglist.support.collection.ScreenshotPreviews
import md.vnastasi.shoppinglist.support.theme.AppTheme

@PreviewTest
@ScreenshotPreviews
@Composable
internal fun OneItemOverviewScreen(
    @PreviewParameter(ShoppingListPreviewParameter::class, limit = 1) shoppingList: ShoppingListDetails
) {
    val viewState = ViewState.Ready(
        data = persistentListOf(ShoppingListUiModel(shoppingList = shoppingList, swipeToRevealState = SwipeToRevealState.Content))
    )

    AppTheme {
        PreviewableSharedTransitionLayout {
            OverviewScreen(
                viewModel = StubOverviewViewModel(viewState),
                onNavigate = { }
            )
        }
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
internal fun MultipleItemsOverviewScreen(
    @PreviewParameter(LisOfShoppingListUiModelsPreviewParameter::class, limit = 1) listOfShoppingListUiModels: List<ShoppingListUiModel>
) {
    val viewState = ViewState.Ready(
        data = listOfShoppingListUiModels.toImmutableList()
    )

    AppTheme {
        PreviewableSharedTransitionLayout {
            OverviewScreen(
                viewModel = StubOverviewViewModel(viewState),
                onNavigate = { }
            )
        }
    }
}
