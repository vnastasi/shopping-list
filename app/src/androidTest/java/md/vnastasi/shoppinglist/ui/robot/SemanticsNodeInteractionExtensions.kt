package md.vnastasi.shoppinglist.ui.robot

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.Dp

fun SemanticsNodeInteraction.performDragUp(offset: Dp): SemanticsNodeInteraction =
    performTouchInput {
        down(center)
        advanceEventTime(500L)
        moveBy(Offset(x = 0.0f, y = -offset.toPx()))
        advanceEventTime(2000L)
        up()
    }

fun SemanticsNodeInteraction.performDragDown(offset: Dp): SemanticsNodeInteraction =
    performTouchInput {
        down(center)
        advanceEventTime(500L)
        moveBy(Offset(x = 0.0f, y = offset.toPx()))
        advanceEventTime(2000L)
        up()
    }

fun SemanticsNodeInteraction.performDragLeft(offset: Dp): SemanticsNodeInteraction =
    performTouchInput {
        down(center)
        advanceEventTime(500L)
        moveBy(Offset(x = -offset.toPx(), y = 0.0f))
        advanceEventTime(2000L)
        up()
    }
