package md.vnastasi.shoppinglist.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class NameSuggestion(
    val id: Long = 0L,
    val name: String
): Parcelable
