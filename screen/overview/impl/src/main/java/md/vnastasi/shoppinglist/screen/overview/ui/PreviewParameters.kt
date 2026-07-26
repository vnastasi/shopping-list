package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.screen.overview.model.ShoppingListUiModel
import md.vnastasi.shoppinglist.screen.overview.model.SwipeToRevealState

private val previewListOfShoppingLists = listOf(
    ShoppingListDetails(id = 1L, name = "Groceries", position = 1L, totalItems = 5L, checkedItems = 1L),
    ShoppingListDetails(id = 2L, name = "Pharmacy for mom", position = 2L, totalItems = 2L, checkedItems = 2L),
    ShoppingListDetails(id = 3L, name = "Gamma & Praxis", position = 3L, totalItems = 12L, checkedItems = 2L),
    ShoppingListDetails(id = 4L, name = "Birthday party shopping list", position = 4L, totalItems = 1L, checkedItems = 0L),
    ShoppingListDetails(id = 5L, name = "Christmas Eve party", position = 5L, totalItems = 0L, checkedItems = 0L),
    ShoppingListDetails(id = 6L, name = "Thanksgiving family reunion", position = 6L, totalItems = 1L, checkedItems = 1L),
    ShoppingListDetails(id = 7L, name = "Ibiza!", position = 7L, totalItems = 1L, checkedItems = 1L),
    ShoppingListDetails(id = 8L, name = "At the baker's", position = 8L, totalItems = 5L, checkedItems = 0L),
    ShoppingListDetails(id = 9L, name = "Big shopping at the mall", position = 9L, totalItems = 5L, checkedItems = 4L),
    ShoppingListDetails(id = 10L, name = "Trip to Iceland", position = 10L, totalItems = 2L, checkedItems = 2L),
    ShoppingListDetails(id = 11L, name = "Disney", position = 11L, totalItems = 23L, checkedItems = 0L),
    ShoppingListDetails(id = 12L, name = "Trip to Paris", position = 12L, totalItems = 14L, checkedItems = 5L),
)

private val previewListOfShoppingListUiModels = previewListOfShoppingLists.mapIndexed { index, shoppingListDetails ->
    val swipeToRevealState = if (index == 1) SwipeToRevealState.Actions else SwipeToRevealState.Content
    ShoppingListUiModel(shoppingListDetails, swipeToRevealState)
}

internal class ShoppingListPreviewParameter : PreviewParameterProvider<ShoppingListDetails> {

    override val values: Sequence<ShoppingListDetails> = sequenceOf(previewListOfShoppingLists[0])
}

internal class LisOfShoppingListUiModelsPreviewParameter : PreviewParameterProvider<ImmutableList<ShoppingListUiModel>> {

    override val values: Sequence<ImmutableList<ShoppingListUiModel>> = sequenceOf(previewListOfShoppingListUiModels.toImmutableList())
}
