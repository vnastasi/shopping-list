package md.vnastasi.shoppinglist.screen.listoverview.ui

class TextFieldValidator {

    fun validate(input: String): Error = when {
        input.isEmpty() -> Error.EMPTY
        input.isBlank() -> Error.BLANK
        else -> Error.NONE
    }

    enum class Error {

        NONE, EMPTY, BLANK;

        fun noErrors() = this == NONE

        fun hasErrors() = this != NONE
    }
}
