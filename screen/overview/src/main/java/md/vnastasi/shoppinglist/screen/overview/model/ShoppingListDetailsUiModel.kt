package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Immutable
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails

@Immutable
data class ShoppingListDetailsUiModel(
    val shoppingListDetails: ShoppingListDetails,
    val swipeToRevealState: SwipeToRevealState
)
