package md.vnastasi.shoppinglist.screen.additems

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.NameSuggestion

@Stable
data class ViewState(
    val searchTerm: String,
    val suggestions: ImmutableList<NameSuggestion>,
    val toastMessage: String?
) {

    companion object {

        fun init() = ViewState(searchTerm = "", suggestions = persistentListOf(), toastMessage = null)
    }
}
