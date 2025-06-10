package md.vnastasi.shoppinglist.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class ShoppingList(
    val id: Long = 0,
    val name: String
): Parcelable
