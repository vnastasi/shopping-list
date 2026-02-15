package md.vnastasi.shoppinglist.screen.managelist.model

data class ViewState(
    val validationError: TextValidationError,
    val isSaveEnabled: Boolean,
    val navigationTarget: NavigationTarget?
) {

    companion object {

        val INIT = ViewState(
            validationError = TextValidationError.NONE,
            isSaveEnabled = false,
            navigationTarget = null
        )
    }
}
