package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import md.vnastasi.shoppinglist.support.theme.AppTypography

@Composable
internal fun ShoppingListCard(
    modifier: Modifier = Modifier,
    list: ShoppingListDetails,
    onClickItem: (ShoppingListDetails) -> Unit = { },
    onDeleteItem: (ShoppingListDetails) -> Unit = { }
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
            positionalThreshold = { it / 2 }
        )

        SwipeToDismissBox(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(
                    start = AppDimensions.paddingSmall,
                    end = AppDimensions.paddingSmall,
                )
                .align(Alignment.Center),
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true,
            state = swipeToDismissBoxState,
            backgroundContent = {
                Card(
                    modifier = Modifier
                        .widthIn(max = AppDimensions.contentMaxWidth)
                        .padding(AppDimensions.paddingSmall),
                    colors = CardDefaults.cardColors().copy(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = CardDefaults.outlinedShape
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(end = AppDimensions.paddingMedium),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = stringResource(R.string.add_items_btn_delete_suggestion_acc)
                        )
                    }
                }
            }
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = AppDimensions.contentMaxWidth)
                    .padding(AppDimensions.paddingSmall),
                shape = CardDefaults.outlinedShape,
                elevation = CardDefaults.elevatedCardElevation()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable { onClickItem.invoke(list) }
                        .padding(AppDimensions.paddingSmall),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(AppDimensions.paddingSmall),
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null
                    )

                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            modifier = Modifier
                                .alignBy(LastBaseline)
                                .weight(1.0f)
                                .padding(start = AppDimensions.paddingSmall),
                            text = list.name,
                            style = AppTypography.titleLarge
                        )

                        Text(
                            modifier = Modifier
                                .alignBy(LastBaseline)
                                .padding(
                                    start = AppDimensions.paddingSmall,
                                    end = AppDimensions.paddingSmall
                                ),
                            text = "${list.checkedItems} / ${list.totalItems}",
                            style = AppTypography.bodySmall
                        )
                    }
                }
            }
        }

        when (swipeToDismissBoxState.currentValue) {
            SwipeToDismissBoxValue.EndToStart -> onDeleteItem.invoke(list)
            else -> Unit
        }
    }
}

@Preview
@Composable
private fun ShoppingListCardPreview() {
    val shoppingList = ShoppingListDetails(1, "Sample shopping list", 0L, 0L)

    AppTheme {
        ShoppingListCard(list = shoppingList)
    }
}
