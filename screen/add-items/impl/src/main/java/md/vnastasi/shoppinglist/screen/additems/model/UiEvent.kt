package md.vnastasi.shoppinglist.screen.additems.model

import androidx.compose.runtime.Immutable
import md.vnastasi.shoppinglist.domain.model.NameSuggestion

@Immutable
internal sealed interface UiEvent {

    @Immutable
    data object OnBackClicked : UiEvent

    @Immutable
    data class OnItemAddedToList(val name: String) : UiEvent

    @Immutable
    data class OnSuggestionDeleted(val suggestion: NameSuggestion) : UiEvent
}
