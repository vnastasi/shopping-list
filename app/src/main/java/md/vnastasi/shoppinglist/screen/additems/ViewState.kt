package md.vnastasi.shoppinglist.screen.additems

import androidx.compose.runtime.Stable
import md.vnastasi.shoppinglist.domain.model.NameSuggestion

@Stable
data class ViewState(
    val searchTerm: String,
    val suggestions: List<NameSuggestion>,
    val toastMessage: String?
) {

    companion object {

        fun init() = ViewState(searchTerm = "", suggestions = emptyList(), toastMessage = null)
    }
}
