package md.vnastasi.shoppinglist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingListDetails(
    val id: Long,
    val name: String,
    val totalItems: Long,
    val checkedItems: Long
): Parcelable {

    fun toShoppingList() = ShoppingList(id, name)
}
