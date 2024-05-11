package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme

@Composable
internal fun NonEmptyListDetailsScreenContent(
    contentPaddings: PaddingValues,
    listOfShoppingItems: ImmutableList<ShoppingItem>,
    onClick: (ShoppingItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppDimensions.paddingSmall),
        contentPadding = PaddingValues(
            start = contentPaddings.calculateStartPadding(LocalLayoutDirection.current),
            end = contentPaddings.calculateEndPadding(LocalLayoutDirection.current),
            top = contentPaddings.calculateTopPadding(),
            bottom = contentPaddings.calculateBottomPadding() + AppDimensions.paddingMedium
        )
    ) {
        itemsIndexed(
            items = listOfShoppingItems,
            key = { _, shoppingItem -> shoppingItem.id }
        ) { index, shoppingItem ->
            ShoppingItemRow(
                shoppingItem = shoppingItem,
                isLastItemInList = index == listOfShoppingItems.size - 1,
                onClick = onClick
            )
        }
    }
}

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
        NonEmptyListDetailsScreenContent(
            contentPaddings = PaddingValues(AppDimensions.zero),
            listOfShoppingItems = listOfShoppingItems,
            onClick = { }
        )
    }
}
