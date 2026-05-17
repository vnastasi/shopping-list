package md.vnastasi.shoppinglist.screen.shared.reorder

import androidx.compose.runtime.Stable

@Stable
sealed class ReorderDragHandleState {

    @Stable
    object Disabled : ReorderDragHandleState()

    @Stable
    class Enabled(val onReorder: () -> Unit) : ReorderDragHandleState()
}
