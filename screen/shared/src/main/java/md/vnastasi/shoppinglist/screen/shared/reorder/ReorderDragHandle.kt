package md.vnastasi.shoppinglist.screen.shared.reorder

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.support.theme.AppIcons
import sh.calvin.reorderable.ReorderableCollectionItemScope

context(reorderableCollectionItemScope: ReorderableCollectionItemScope, rowScope: RowScope)
@Composable
fun ReorderDragHandle(
    state: ReorderDragHandleState
) {
    with(rowScope) {
        AnimatedContent(
            targetState = state,
            contentKey = { it::class },
            contentAlignment = Alignment.CenterEnd
        ) { reorderableState ->
            when (reorderableState) {
                is ReorderDragHandleState.Disabled -> {
                    Spacer(modifier = Modifier)
                }

                is ReorderDragHandleState.Enabled -> {
                    with(reorderableCollectionItemScope) {
                        IconButton(
                            modifier = Modifier.draggableHandle(
                                onDragStopped = reorderableState.onReorder,
                            ),
                            onClick = { }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically),
                                imageVector = AppIcons.DragHandle,
                                contentDescription = stringResource(R.string.overview_item_drag_handle_btn_acc)
                            )
                        }
                    }
                }
            }
        }
    }
}
