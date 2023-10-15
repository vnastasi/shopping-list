package md.vnastasi.shoppinglist.screen.additems

data class ViewState(
    val searchTerm: String,
    val suggestions: List<String>,
    val toastMessage: String?
) {

    companion object {

        fun init() = ViewState(searchTerm = "", suggestions = emptyList(), toastMessage = null)
    }
}
