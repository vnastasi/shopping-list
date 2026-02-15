package md.vnastasi.shoppinglist.screen.managelist.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface UiEvent {

    @Immutable
    data class OnNameChanged(val name: String) : UiEvent

    @Immutable
    data class OnNameSaved(val name: String) : UiEvent

    @Immutable
    data object OnNavigationConsumed : UiEvent
}
