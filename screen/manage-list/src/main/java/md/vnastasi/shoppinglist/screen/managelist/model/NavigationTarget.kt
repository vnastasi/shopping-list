package md.vnastasi.shoppinglist.screen.managelist.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface NavigationTarget {

    @Immutable
    data object CloseSheet : NavigationTarget
}
