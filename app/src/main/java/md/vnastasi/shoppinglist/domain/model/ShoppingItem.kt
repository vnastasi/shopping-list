package md.vnastasi.shoppinglist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingItem(
    val id: Long,
    val name: String,
    val isChecked: Boolean,
    val list: ShoppingList
): Parcelable
