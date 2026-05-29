package md.vnastasi.shoppinglist.screen.listdetails.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface NavigationTarget {

    @Immutable
    data object BackToOverview : NavigationTarget

    @Immutable
    data class AddItems(val shoppingListId: Long) : NavigationTarget
}
