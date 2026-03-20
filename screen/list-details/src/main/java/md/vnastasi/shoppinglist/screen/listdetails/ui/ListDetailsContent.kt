package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.SHOPPING_ITEMS_ITEM
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.SHOPPING_ITEMS_LIST
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
internal fun ListDetailsContent(
    contentPaddings: PaddingValues,
    listOfShoppingItems: ImmutableList<ShoppingItem>,
    dispatchEvent: (UiEvent) -> Unit,
) {
    val reorderableList = remember(listOfShoppingItems) { listOfShoppingItems.toMutableStateList() }
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        reorderableList.add(to.index, reorderableList.removeAt(from.index))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = AppDimensions.paddingSmall)
            .testTag(SHOPPING_ITEMS_LIST),
        state = lazyListState,
        contentPadding = PaddingValues(
            start = contentPaddings.calculateStartPadding(LocalLayoutDirection.current),
            end = contentPaddings.calculateEndPadding(LocalLayoutDirection.current),
            top = contentPaddings.calculateTopPadding(),
            bottom = contentPaddings.calculateBottomPadding() + AppDimensions.paddingMedium
        )
    ) {
        itemsIndexed(
            items = reorderableList,
            key = { _, shoppingItem -> shoppingItem.id }
        ) { index, shoppingItem ->
            ReorderableItem(
                state = reorderableLazyListState,
                key = shoppingItem.id
            ) {
                ShoppingItemRow(
                    modifier = Modifier
                        .animateItem()
                        .testTag(SHOPPING_ITEMS_ITEM),
                    shoppingItem = shoppingItem,
                    onClick = { dispatchEvent(UiEvent.OnItemClicked(it)) },
                    onDelete = { dispatchEvent(UiEvent.OnItemDeleted(it)) },
                    onReorderItem = { dispatchEvent(UiEvent.OnItemsReordered(reorderableList)) }
                )
            }

            if (index < reorderableList.lastIndex) {
                CenterAlignedDivider()
            }
        }
    }
}

@Composable
private fun CenterAlignedDivider() {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = AppDimensions.contentMaxWidth)
                .padding(
                    start = AppDimensions.paddingSmall,
                    end = AppDimensions.paddingSmall
                ),
            thickness = AppDimensions.divider
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
private fun NonEmptyListDetailsScreenContentPreview() {
    val shoppingList = ShoppingList(id = 1L, "My list")
    val listOfShoppingItems = persistentListOf(
        ShoppingItem(id = 1L, name = "Apples", isChecked = true, list = shoppingList),
        ShoppingItem(id = 2L, name = "Bread", isChecked = false, list = shoppingList),
        ShoppingItem(id = 3L, name = "Minced meat", isChecked = true, list = shoppingList),
        ShoppingItem(id = 4L, name = "Deodorant", isChecked = false, list = shoppingList),
    )

    AppTheme {
        ListDetailsContent(
            contentPaddings = PaddingValues(AppDimensions.zero),
            listOfShoppingItems = listOfShoppingItems,
            dispatchEvent = { },
        )
    }
}
