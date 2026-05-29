package md.vnastasi.shoppinglist.screen.managelist.model

import androidx.compose.runtime.Immutable

@Immutable
data class ViewState(
    val validationError: TextValidationError,
    val isSaveEnabled: Boolean
) {

    companion object {

        val INIT = ViewState(
            validationError = TextValidationError.NONE,
            isSaveEnabled = false
        )
    }
}
