package md.vnastasi.shoppinglist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NameSuggestion(
    val id: Long = 0L,
    val name: String
): Parcelable
