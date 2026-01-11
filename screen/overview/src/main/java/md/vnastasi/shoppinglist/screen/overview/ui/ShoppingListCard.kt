package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import md.vnastasi.shoppinglist.support.theme.AppTypography
import kotlin.math.roundToInt

private enum class SwipeToRevealState {

    Resting, Actions
}

@Composable
internal fun ShoppingListCard(
    modifier: Modifier = Modifier,
    item: ShoppingListDetails,
    onClickItem: (ShoppingListDetails) -> Unit = { },
    onDeleteItem: (ShoppingListDetails) -> Unit = { }
) {
    val density = LocalDensity.current
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()

    val dragState = remember {
        val actionOffset = with(density) { 100.dp.toPx() }
        AnchoredDraggableState(
            initialValue = SwipeToRevealState.Resting,
            anchors = DraggableAnchors {
                SwipeToRevealState.Resting at 0.0f
                SwipeToRevealState.Actions at -actionOffset
            },
            positionalThreshold = { it },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec
        )
    }

    val overScrollEffect = rememberOverscrollEffect()

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .anchoredDraggable(
                    state = dragState,
                    orientation = Orientation.Horizontal,
                    overscrollEffect = overScrollEffect,
                )
                .overscroll(overScrollEffect)
                .offset {
                    IntOffset(
                        x = dragState.requireOffset().roundToInt(),
                        y = 0
                    )
                }

        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = AppDimensions.contentMaxWidth)
                    .padding(
                        horizontal = AppDimensions.paddingMedium,
                        vertical = AppDimensions.paddingSmall
                    )
                    .align(Alignment.Center),
                shape = CardDefaults.outlinedShape,
                elevation = CardDefaults.elevatedCardElevation()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable { onClickItem.invoke(item) }
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
                            text = item.name,
                            style = AppTypography.titleLarge
                        )
                        Text(
                            modifier = Modifier
                                .alignBy(LastBaseline)
                                .padding(
                                    start = AppDimensions.paddingSmall,
                                    end = AppDimensions.paddingSmall
                                ),
                            text = "${item.checkedItems} / ${item.totalItems}",
                            style = AppTypography.bodySmall
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .widthIn(max = AppDimensions.contentMaxWidth)
                .padding(
                    horizontal = AppDimensions.paddingMedium,
                    vertical = AppDimensions.paddingSmall
                )
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier.weight(1.0f)
            )

            AnimatedVisibility(
                visible = dragState.currentValue == SwipeToRevealState.Actions,
                enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center)
            ) {
                IconButton(
                    shape = MaterialTheme.shapes.medium,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    onClick = { onDeleteItem.invoke(item) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = stringResource(R.string.add_items_btn_delete_suggestion_acc)
                    )
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
private fun ShoppingListCardPreview() {
    val shoppingList = ShoppingListDetails(1, "Sample shopping list", 0L, 0L)

    AppTheme {
        ShoppingListCard(item = shoppingList)
    }
}
