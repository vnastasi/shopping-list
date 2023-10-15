package md.vnastasi.shoppinglist.screen.additems

import md.vnastasi.shoppinglist.domain.model.NameSuggestion

data class ViewState(
    val searchTerm: String,
    val suggestions: List<NameSuggestion>,
    val toastMessage: String?
) {

    companion object {

        fun init() = ViewState(searchTerm = "", suggestions = emptyList(), toastMessage = null)
    }
}
