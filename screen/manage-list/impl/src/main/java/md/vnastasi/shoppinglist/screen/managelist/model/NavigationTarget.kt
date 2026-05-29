package md.vnastasi.shoppinglist.screen.managelist.model

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface NavigationTarget {

    @Immutable
    data class CloseSheet(val affectedShoppingListId: Long?) : NavigationTarget
}
