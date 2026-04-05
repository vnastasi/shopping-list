package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.LIST_ITEM_CHECKBOX
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.SHOPPING_ITEMS_ITEM_DELETE_BUTTON
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppIcons
import md.vnastasi.shoppinglist.support.theme.AppTheme
import md.vnastasi.shoppinglist.support.theme.AppTypography
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
internal fun ReorderableCollectionItemScope.ShoppingItemRow(
    modifier: Modifier = Modifier,
    shoppingItem: ShoppingItem,
    onClick: (ShoppingItem) -> Unit,
    onDelete: (ShoppingItem) -> Unit,
    onReorderItem: () -> Unit = { }
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = AppDimensions.contentMaxWidth)
                .align(Alignment.Center)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(shoppingItem) }
                    .padding(AppDimensions.paddingSmall)
            ) {
                Checkbox(
                    checked = shoppingItem.isChecked,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .testTag(LIST_ITEM_CHECKBOX),
                    onCheckedChange = { onClick(shoppingItem) },
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
                    onClick = { onDelete(shoppingItem) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.add_items_btn_delete_suggestion_acc),
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                IconButton(
                    modifier = Modifier.draggableHandle(
                        onDragStopped = onReorderItem,
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


    AppTheme {
        LazyColumn {
            item {
                ReorderableItem(
                    state = rememberReorderableLazyListState(rememberLazyListState()) { _, _ -> },
                    key = Unit
                ) {
                    ShoppingItemRow(
                        shoppingItem = shoppingItem,
                        onClick = { },
                        onDelete = { }
                    )
                }
            }
        }
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

    AppTheme {
        LazyColumn {
            item {
                ReorderableItem(
                    state = rememberReorderableLazyListState(rememberLazyListState()) { _, _ -> },
                    key = Unit
                ) {
                    ShoppingItemRow(
                        shoppingItem = shoppingItem,
                        onClick = { },
                        onDelete = { }
                    )
                }
            }
        }
    }
}
