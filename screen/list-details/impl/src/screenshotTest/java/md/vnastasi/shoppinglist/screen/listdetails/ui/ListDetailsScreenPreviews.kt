package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.transition.PreviewableSharedTransitionLayout
import md.vnastasi.shoppinglist.support.collection.ScreenshotPreviews
import md.vnastasi.shoppinglist.support.theme.AppTheme

@PreviewTest
@ScreenshotPreviews
@Composable
internal fun OneItemListDetailsScreen(
    @PreviewParameter(ShoppingItemParameterProvider::class, limit = 1) shoppingItem: ShoppingItem
) {
    val viewState = ViewState.Ready(
        shoppingListId = previewShoppingList.id,
        shoppingListName = previewShoppingList.name,
        listOfShoppingItems = persistentListOf(shoppingItem)
    )
    AppTheme {
        PreviewableSharedTransitionLayout {
            ListDetailsScreen(
                viewModel = StubListDetailsViewModelSpec(viewState),
                onNavigate = { }
            )
        }
    }
}

@PreviewTest
@ScreenshotPreviews
@Composable
internal fun MultipleItemsListDetailsScreen(
    @PreviewParameter(ListOfShoppingItemsParameterProvider::class, limit = 1) shoppingItems: List<ShoppingItem>
) {
    val viewState = ViewState.Ready(
        shoppingListId = previewShoppingList.id,
        shoppingListName = previewShoppingList.name,
        listOfShoppingItems = shoppingItems.toImmutableList()
    )
    AppTheme {
        PreviewableSharedTransitionLayout {
            ListDetailsScreen(
                viewModel = StubListDetailsViewModelSpec(viewState),
                onNavigate = { }
            )
        }
    }
}
