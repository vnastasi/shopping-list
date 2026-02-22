package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.SHOPPING_LISTS_ITEM
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.SHOPPING_LISTS_LIST
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
internal fun OverviewContent(
    contentPaddings: PaddingValues,
    list: ImmutableList<ShoppingListDetails>,
    dispatchEvent: (UiEvent) -> Unit
) {
    val reorderableList = remember(list) { list.toMutableStateList() }
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        reorderableList.add(to.index, reorderableList.removeAt(from.index))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(SHOPPING_LISTS_LIST),
        state = lazyListState,
        contentPadding = PaddingValues(
            start = contentPaddings.calculateStartPadding(LocalLayoutDirection.current),
            end = contentPaddings.calculateEndPadding(LocalLayoutDirection.current),
            top = contentPaddings.calculateTopPadding(),
            bottom = contentPaddings.calculateBottomPadding() + AppDimensions.paddingMedium
        )
    ) {
        items(
            items = reorderableList,
            key = { it.id }
        ) { shoppingList ->
            ReorderableItem(
                state = reorderableLazyListState,
                key = shoppingList.id
            ) {
                ShoppingListCard(
                    modifier = Modifier
                        .animateItem()
                        .testTag(SHOPPING_LISTS_ITEM),
                    item = shoppingList,
                    onEditItem = { dispatchEvent(UiEvent.OnShoppingListEdited(shoppingList)) },
                    onClickItem = { dispatchEvent(UiEvent.OnShoppingListSelected(shoppingList)) },
                    onDeleteItem = { dispatchEvent(UiEvent.OnShoppingListDeleted(shoppingList)) },
                    onReorderItem = { dispatchEvent(UiEvent.OnShoppingListsReordered(reorderableList)) }
                )
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
private fun NonEmptyListOverviewScreenContentPreview() {
    val list = persistentListOf(
        ShoppingListDetails(id = 1L, name = "Groceries", position = 1L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 2L, name = "Pharmacy for mom", position = 2L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 3L, name = "Gamma & Praxis", position = 3L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 4L, name = "Birthday party shopping list", position = 4L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 5L, name = "Christmas Eve party", position = 5L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 6L, name = "Thanksgiving family reunion", position = 6L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 7L, name = "Ibiza!", position = 7L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 8L, name = "At the baker's", position = 8L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 9L, name = "Big shopping at the mall", position = 9L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 10L, name = "Trip to Iceland", position = 10L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 11L, name = "Disney", position = 11L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 12L, name = "Trip to Paris", position = 12L, totalItems = 0L, checkedItems = 0L),
    )

    AppTheme {
        OverviewContent(
            contentPaddings = PaddingValues(AppDimensions.zero),
            list = list,
            dispatchEvent = { }
        )
    }
}
