package md.vnastasi.shoppinglist.screen.additems

import md.vnastasi.shoppinglist.domain.model.NameSuggestion

sealed interface UiEvent {

    data class SearchTermChanged(val newSearchTerm: String) : UiEvent

    data class ItemAddedToList(val name: String): UiEvent

    data class SuggestionDeleted(val suggestion: NameSuggestion): UiEvent

    data object ToastShown : UiEvent
}
