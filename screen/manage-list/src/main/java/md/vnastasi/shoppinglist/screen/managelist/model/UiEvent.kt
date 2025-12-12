package md.vnastasi.shoppinglist.screen.managelist.model

sealed interface UiEvent {

    data class OnNameChange(val name: String) : UiEvent

    data class OnSaveList(val name: String) : UiEvent
}
