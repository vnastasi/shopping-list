package md.vnastasi.shoppinglist.screen.managelist.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface NavigationTarget {

    @Immutable
    data class CloseSheet(val affectedShoppingListId: Long?) : NavigationTarget
}
