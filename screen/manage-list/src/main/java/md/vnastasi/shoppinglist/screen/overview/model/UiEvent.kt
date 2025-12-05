package md.vnastasi.shoppinglist.screen.overview.model

sealed interface UiEvent {

    data object Saved : UiEvent
}
