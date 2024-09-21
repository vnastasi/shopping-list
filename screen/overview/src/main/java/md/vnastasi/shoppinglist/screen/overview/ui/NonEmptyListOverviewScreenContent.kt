package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.SHOPPING_LISTS_ITEM
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.SHOPPING_LISTS_LIST
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme

@Composable
internal fun NonEmptyListOverviewScreenContent(
    contentPaddings: PaddingValues,
    list: ImmutableList<ShoppingListDetails>,
    onDelete: (ShoppingListDetails) -> Unit,
    onClick: (ShoppingListDetails) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(SHOPPING_LISTS_LIST),
        contentPadding = PaddingValues(
            start = contentPaddings.calculateStartPadding(LocalLayoutDirection.current),
            end = contentPaddings.calculateEndPadding(LocalLayoutDirection.current),
            top = contentPaddings.calculateTopPadding(),
            bottom = contentPaddings.calculateBottomPadding() + AppDimensions.paddingMedium
        )
    ) {
        items(items = list, key = { it.id }) { shoppingList ->
            ShoppingListCard(
                modifier = Modifier.testTag(SHOPPING_LISTS_ITEM),
                list = shoppingList,
                onClickItem = onClick,
                onDeleteItem = onDelete
            )
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
        ShoppingListDetails(id = 1L, name = "Groceries", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 2L, name = "Pharmacy for mom", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 3L, name = "Gamma & Praxis", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 4L, name = "Birthday party shopping list", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 5L, name = "Christmas Eve party", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 6L, name = "Thanksgiving family reunion", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 7L, name = "Ibiza!", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 8L, name = "At the baker's", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 9L, name = "Big shopping at the mall", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 10L, name = "Trip to Iceland", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 11L, name = "Disney", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 12L, name = "Trip to Paris", totalItems = 0L, checkedItems = 0L),
    )

    AppTheme {
        NonEmptyListOverviewScreenContent(
            contentPaddings = PaddingValues(AppDimensions.zero),
            list = list,
            onDelete = { },
            onClick = { }
        )
    }
}
