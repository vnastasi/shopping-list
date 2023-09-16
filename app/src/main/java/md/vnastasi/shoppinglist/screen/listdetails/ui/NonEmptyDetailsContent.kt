package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingItem

@Composable
fun NonEmptyDetailsContent(
    contentPaddings: PaddingValues,
    listOfShoppingItems: ImmutableList<ShoppingItem>,
    onClick: (ShoppingItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(
                top = contentPaddings.calculateTopPadding(),
                bottom = contentPaddings.calculateBottomPadding()
            )
            .fillMaxWidth()
    ) {
        itemsIndexed(
            items = listOfShoppingItems,
            key = { _, shoppingItem -> shoppingItem.id }
        ) { index, shoppingItem ->
            ShoppingItemRow(
                shoppingItem = shoppingItem,
                isLastItemInList = remember { mutableStateOf(index == listOfShoppingItems.size - 1) },
                onClick = onClick
            )
        }
    }
}
