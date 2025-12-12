package md.vnastasi.shoppinglist.screen.managelist.model

data class ViewState(
    val name: String,
    val validationError: TextValidationError,
    val isSaveEnabled: Boolean
) {

    companion object {

        val INIT = ViewState(
            name = "",
            validationError = TextValidationError.NONE,
            isSaveEnabled = false
        )
    }
}
