package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.LIST_ITEM_CHECKBOX
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.SHOPPING_ITEMS_ITEM_DELETE_BUTTON
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTypography
import sh.calvin.reorderable.DragGestureDetector
import sh.calvin.reorderable.ReorderableCollectionItemScope

@Composable
internal fun ReorderableCollectionItemScope.ShoppingItemRow(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource,
    shoppingItem: ShoppingItem,
    isLastItemInList: Boolean,
    onClicked: (ShoppingItem) -> Unit,
    onDeleted: (ShoppingItem) -> Unit,
    onOrderChanged: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = AppDimensions.contentMaxWidth)
                .align(Alignment.Center)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(interactionSource = interactionSource) { onClicked.invoke(shoppingItem) }
                    .padding(AppDimensions.paddingSmall)
            ) {
                Checkbox(
                    checked = shoppingItem.isChecked,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .testTag(LIST_ITEM_CHECKBOX),
                    onCheckedChange = { onClicked.invoke(shoppingItem) },
                    colors = CheckboxDefaults.colors().copy(
                        checkedBoxColor = MaterialTheme.colorScheme.tertiary,
                        checkedBorderColor = MaterialTheme.colorScheme.tertiary,
                    )
                )

                Text(
                    modifier = Modifier
                        .padding(start = AppDimensions.paddingSmall)
                        .align(Alignment.CenterVertically),
                    text = shoppingItem.name,
                    style = AppTypography.bodyLarge.copy(
                        textDecoration = if (shoppingItem.isChecked) TextDecoration.LineThrough else TextDecoration.None
                    )
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                IconButton(
                    modifier = Modifier
                        .testTag(SHOPPING_ITEMS_ITEM_DELETE_BUTTON)
                        .wrapContentSize()
                        .align(Alignment.CenterVertically),
                    onClick = { onDeleted.invoke(shoppingItem) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.add_items_btn_delete_suggestion_acc),
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                IconButton(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterVertically)
                        .draggableHandle(
                            interactionSource = interactionSource,
                            onDragStopped = onOrderChanged
                        )
                        .clearAndSetSemantics {
                            testTag = SHOPPING_ITEMS_ITEM_DELETE_BUTTON
                        },
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.drag_indicator),
                        contentDescription = stringResource(R.string.add_items_btn_delete_suggestion_acc),
                    )
                }
            }

            if (!isLastItemInList) {
                HorizontalDivider(
                    modifier = Modifier.padding(
                        start = AppDimensions.paddingSmall,
                        end = AppDimensions.paddingSmall
                    ),
                    thickness = AppDimensions.divider
                )
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    name = "Checked item",
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
private fun ShoppingItemRowPreview1() {
    val shoppingItem = ShoppingItem(
        id = 1,
        name = "Sample shopping item",
        isChecked = true,
        list = ShoppingList(1, "list")
    )
    with(PreviewReorderableCollectionItemScope) {
        ShoppingItemRow(
            interactionSource = remember { MutableInteractionSource() },
            shoppingItem = shoppingItem,
            isLastItemInList = true,
            onClicked = { },
            onDeleted = { },
            onOrderChanged = { }
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    name = "Unchecked item",
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
private fun ShoppingItemRowPreview2() {
    val shoppingItem = ShoppingItem(
        id = 1,
        name = "Sample shopping item",
        isChecked = false,
        list = ShoppingList(1, "list")
    )
    with(PreviewReorderableCollectionItemScope) {
        ShoppingItemRow(
            interactionSource = remember { MutableInteractionSource() },
            shoppingItem = shoppingItem,
            isLastItemInList = true,
            onClicked = { },
            onDeleted = { },
            onOrderChanged = { }
        )
    }
}

@ExcludeFromJacocoGeneratedReport
private object PreviewReorderableCollectionItemScope : ReorderableCollectionItemScope {

    override fun Modifier.draggableHandle(
        enabled: Boolean,
        interactionSource: MutableInteractionSource?,
        onDragStarted: (Offset) -> Unit,
        onDragStopped: () -> Unit,
        dragGestureDetector: DragGestureDetector
    ): Modifier = this

    override fun Modifier.longPressDraggableHandle(
        enabled: Boolean,
        interactionSource: MutableInteractionSource?,
        onDragStarted: (Offset) -> Unit,
        onDragStopped: () -> Unit
    ): Modifier = this
}
