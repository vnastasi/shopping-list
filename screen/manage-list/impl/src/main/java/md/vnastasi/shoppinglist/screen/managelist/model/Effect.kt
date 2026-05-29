package md.vnastasi.shoppinglist.screen.managelist.model

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface Effect {

    @Immutable
    data class Navigation(val target: NavigationTarget) : Effect
}
