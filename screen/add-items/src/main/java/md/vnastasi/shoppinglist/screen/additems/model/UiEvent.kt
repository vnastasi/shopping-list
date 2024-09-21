package md.vnastasi.shoppinglist.screen.additems.model

import md.vnastasi.shoppinglist.domain.model.NameSuggestion

sealed interface UiEvent {

    data object SearchTermChanged : UiEvent

    data class ItemAddedToList(val name: String): UiEvent

    data class SuggestionDeleted(val suggestion: NameSuggestion): UiEvent

    data object ToastShown : UiEvent
}
