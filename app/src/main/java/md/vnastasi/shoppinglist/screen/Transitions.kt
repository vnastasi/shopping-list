package md.vnastasi.shoppinglist.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween

private const val TRANSITION_DURATION = 400

internal fun AnimatedContentTransitionScope<*>.slideInFromRight() =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(TRANSITION_DURATION)
    )

internal fun AnimatedContentTransitionScope<*>.slideInFromLeft() =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(TRANSITION_DURATION)
    )


internal fun AnimatedContentTransitionScope<*>.slideInFromDown() =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(TRANSITION_DURATION)
    )

internal fun AnimatedContentTransitionScope<*>.slideOutToRight() =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(TRANSITION_DURATION)
    )

internal fun AnimatedContentTransitionScope<*>.slideOutToLeft() =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(TRANSITION_DURATION)
    )

internal fun AnimatedContentTransitionScope<*>.slideOutToDown() =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(TRANSITION_DURATION)
    )
