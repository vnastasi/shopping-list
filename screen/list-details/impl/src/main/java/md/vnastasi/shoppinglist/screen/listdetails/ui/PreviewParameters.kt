package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList

internal val previewShoppingList = ShoppingList(id = 1L, name = "Shopping list")

private val previewShoppingItems = listOf(
    ShoppingItem(id = 1L, name = "Apples", isChecked = false, position = 0L, list = previewShoppingList),
    ShoppingItem(id = 2L, name = "Bread", isChecked = true, position = 1L, list = previewShoppingList),
    ShoppingItem(id = 3L, name = "Minced meat", isChecked = false, position = 2L, list = previewShoppingList),
    ShoppingItem(id = 4L, name = "Deodorant", isChecked = true, position = 3L, list = previewShoppingList),
    ShoppingItem(id = 5L, name = "Milk", isChecked = false, position = 4L, list = previewShoppingList),
    ShoppingItem(id = 6L, name = "Bananas", isChecked = true, position = 5L, list = previewShoppingList),
    ShoppingItem(id = 7L, name = "Potatoes", isChecked = false, position = 6L, list = previewShoppingList),
    ShoppingItem(id = 8L, name = "Cheese", isChecked = true, position = 7L, list = previewShoppingList),
    ShoppingItem(id = 9L, name = "Tomatoes", isChecked = false, position = 8L, list = previewShoppingList),
    ShoppingItem(id = 10L, name = "Yogurt", isChecked = true, position = 9L, list = previewShoppingList),
    ShoppingItem(id = 11L, name = "Eggs", isChecked = false, position = 10L, list = previewShoppingList),
    ShoppingItem(id = 12L, name = "Rice", isChecked = true, position = 11L, list = previewShoppingList),
    ShoppingItem(id = 13L, name = "Pasta", isChecked = false, position = 12L, list = previewShoppingList),
    ShoppingItem(id = 14L, name = "Onions", isChecked = true, position = 13L, list = previewShoppingList)
)

internal class ShoppingItemParameterProvider : PreviewParameterProvider<ShoppingItem> {

    override val values: Sequence<ShoppingItem> = sequenceOf(previewShoppingItems[0])
}

internal class ListOfShoppingItemsParameterProvider : PreviewParameterProvider<ImmutableList<ShoppingItem>> {

    override val values: Sequence<ImmutableList<ShoppingItem>> = sequenceOf(previewShoppingItems.toImmutableList())
}
