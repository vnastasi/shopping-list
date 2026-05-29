package md.vnastasi.shoppinglist.screen.overview.model

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface NavigationTarget {

    @Immutable
    data class AddOrEditList(val shoppingListId: Long?) : NavigationTarget

    @Immutable
    data class ListDetails(val shoppingListId: Long) : NavigationTarget
}
