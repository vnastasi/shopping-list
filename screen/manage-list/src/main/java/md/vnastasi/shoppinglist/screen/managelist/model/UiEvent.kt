package md.vnastasi.shoppinglist.screen.managelist.model

sealed interface UiEvent {

    data object Saved : UiEvent
}
