package md.vnastasi.shoppinglist.screen.shared.reorderable

import androidx.compose.runtime.Stable

@Stable
sealed class ReorderableState {

    @Stable
    object Disabled : ReorderableState()

    @Stable
    class Enabled(val onReorder: () -> Unit) : ReorderableState()
}
