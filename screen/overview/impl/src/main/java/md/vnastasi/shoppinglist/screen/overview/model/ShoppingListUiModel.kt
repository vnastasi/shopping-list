package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Immutable
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails

@Immutable
data class ShoppingListUiModel(
    val shoppingList: ShoppingListDetails,
    val swipeToRevealState: SwipeToRevealState
)
