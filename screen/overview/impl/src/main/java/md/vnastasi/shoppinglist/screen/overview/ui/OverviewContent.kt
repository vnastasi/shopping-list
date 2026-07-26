package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import kotlinx.collections.immutable.ImmutableList
import md.vnastasi.shoppinglist.screen.overview.model.ShoppingListUiModel
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.SHOPPING_LISTS_ITEM
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.SHOPPING_LISTS_LIST
import md.vnastasi.shoppinglist.screen.shared.reorder.ReorderDragHandleState
import md.vnastasi.shoppinglist.screen.shared.transition.ExtendedSharedTransitionScope
import md.vnastasi.shoppinglist.screen.shared.transition.PreviewableSharedTransitionLayout
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

context(extendedSharedTransitionScope: ExtendedSharedTransitionScope)
@Composable
internal fun OverviewContent(
    contentPaddings: PaddingValues,
    list: ImmutableList<ShoppingListUiModel>,
    dispatchEvent: (UiEvent) -> Unit
) {
    val reorderableList = remember(list) { list.toMutableStateList() }
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        reorderableList.add(to.index, reorderableList.removeAt(from.index))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag(SHOPPING_LISTS_LIST),
        state = lazyListState,
        contentPadding = PaddingValues(
            start = contentPaddings.calculateStartPadding(LocalLayoutDirection.current),
            end = contentPaddings.calculateEndPadding(LocalLayoutDirection.current),
            top = contentPaddings.calculateTopPadding(),
            bottom = contentPaddings.calculateBottomPadding() + AppDimensions.paddingMedium
        )
    ) {
        items(
            items = reorderableList,
            key = { it.shoppingList.id }
        ) { shoppingListUiModel ->
            ReorderableItem(
                state = reorderableLazyListState,
                key = shoppingListUiModel.shoppingList.id
            ) {
                val reorderDragHandleState = remember(reorderableList.size) {
                    if (reorderableList.size > 1) {
                        ReorderDragHandleState.Enabled(
                            onReorder = { dispatchEvent(UiEvent.OnShoppingListsReordered(reorderableList)) }
                        )
                    } else {
                        ReorderDragHandleState.Disabled
                    }
                }
                ShoppingListCard(
                    modifier = Modifier
                        .animateItem()
                        .testTag(SHOPPING_LISTS_ITEM),
                    shoppingListUiModel = shoppingListUiModel,
                    reorderDragHandleState = reorderDragHandleState,
                    onEditItem = { dispatchEvent(UiEvent.OnShoppingListEdited(shoppingListUiModel)) },
                    onClickItem = { dispatchEvent(UiEvent.OnShoppingListSelected(shoppingListUiModel)) },
                    onDeleteItem = { dispatchEvent(UiEvent.OnShoppingListDeleted(shoppingListUiModel)) },
                    onSwipeToRevealStateChanged = { dispatchEvent(UiEvent.OnSwipeToRevealStateChanged(shoppingListUiModel.shoppingList.id, it)) }
                )
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
private fun NonEmptyListOverviewScreenContentPreview(
    @PreviewParameter(LisOfShoppingListUiModelsPreviewParameter::class, limit = 1) list: ImmutableList<ShoppingListUiModel>
) {
    AppTheme {
        PreviewableSharedTransitionLayout {
            OverviewContent(
                contentPaddings = PaddingValues(AppDimensions.zero),
                list = list,
                dispatchEvent = { }
            )
        }
    }
}
