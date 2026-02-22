package md.vnastasi.shoppinglist.screen.additems.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface NavigationTarget {

    @Immutable
    data object BackToListDetails : NavigationTarget
}
