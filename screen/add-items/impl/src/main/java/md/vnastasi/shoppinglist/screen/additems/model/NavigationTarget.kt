package md.vnastasi.shoppinglist.screen.additems.model

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface NavigationTarget {

    @Immutable
    data object BackToListDetails : NavigationTarget
}
