package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme

@Composable
internal fun NonEmptyListOverviewScreenContent(
    contentPaddings: PaddingValues,
    list: ImmutableList<ShoppingList>,
    onDelete: (ShoppingList) -> Unit,
    onClick: (ShoppingList) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(
                top = contentPaddings.calculateTopPadding(),
                bottom = contentPaddings.calculateBottomPadding()
            )
            .fillMaxWidth()
    ) {
        items(items = list, key = { it.id }) { shoppingList ->
            ShoppingListCard(
                list = shoppingList,
                onClickItem = onClick,
                onDeleteItem = onDelete
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
private fun NonEmptyListOverviewScreenContentPreview() {
    val list = persistentListOf(
        ShoppingList(id = 1L, name = "Groceries"),
        ShoppingList(id = 2L, name = "Pharmacy for mom"),
        ShoppingList(id = 3L, name = "Gamma & Praxis"),
        ShoppingList(id = 4L, name = "Birthday party shopping list"),
        ShoppingList(id = 5L, name = "Christmas Eve party"),
        ShoppingList(id = 6L, name = "Thanksgiving family reunion"),
        ShoppingList(id = 7L, name = "Ibiza!"),
        ShoppingList(id = 8L, name = "At the baker's"),
        ShoppingList(id = 9L, name = "Big shopping at the mall"),
        ShoppingList(id = 10L, name = "Trip to Iceland"),
        ShoppingList(id = 11L, name = "Disney"),
        ShoppingList(id = 12L, name = "Trip to Paris"),
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
