package md.vnastasi.shoppinglist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingList(
    val id: Long,
    val name: String
): Parcelable
