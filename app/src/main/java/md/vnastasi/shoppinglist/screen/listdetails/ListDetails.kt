package md.vnastasi.shoppinglist.screen.listdetails

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import md.vnastasi.shoppinglist.domain.model.ShoppingItem

@Stable
data class ListDetails(
    val id: Long,
    val name: String,
    val listOfShoppingItems: ImmutableList<ShoppingItem>
)
