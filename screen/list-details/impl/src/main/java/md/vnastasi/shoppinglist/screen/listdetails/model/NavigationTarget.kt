package md.vnastasi.shoppinglist.screen.listdetails.model

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface NavigationTarget {

    @Immutable
    data object BackToOverview : NavigationTarget

    @Immutable
    data class AddItems(val shoppingListId: Long) : NavigationTarget
}
