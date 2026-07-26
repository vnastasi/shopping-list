package md.vnastasi.shoppinglist.screen.shared.reorder

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun PreviewableReorderableItem(
    content: @Composable ReorderableCollectionItemScope.(Boolean) -> Unit
) {
    LazyColumn {
        item {
            ReorderableItem(
                state = rememberReorderableLazyListState(rememberLazyListState()) { _, _ -> },
                key = Unit,
                content = content
            )
        }
    }
}
