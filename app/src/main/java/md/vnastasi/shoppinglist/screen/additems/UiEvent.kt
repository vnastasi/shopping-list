package md.vnastasi.shoppinglist.screen.additems

sealed interface UiEvent {

    data class OnSearchTermChanged(val newSearchTerm: String) : UiEvent

    data class OnAddItem(val name: String): UiEvent
}
