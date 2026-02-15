package md.vnastasi.shoppinglist.screen.managelist.model

sealed interface UiEvent {

    data class OnNameChanged(val name: String) : UiEvent

    data class OnNameSaved(val name: String) : UiEvent

    data object OnNavigationConsumed : UiEvent
}
