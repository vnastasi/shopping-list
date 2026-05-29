package md.vnastasi.shoppinglist.screen.shared.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween

private const val TRANSITION_DURATION = 400

fun AnimatedContentTransitionScope<*>.slideInFromRight() =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(TRANSITION_DURATION)
    )

fun AnimatedContentTransitionScope<*>.slideInFromLeft() =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(TRANSITION_DURATION)
    )


fun AnimatedContentTransitionScope<*>.slideInFromDown() =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(TRANSITION_DURATION)
    )

fun AnimatedContentTransitionScope<*>.slideOutToRight() =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(TRANSITION_DURATION)
    )

fun AnimatedContentTransitionScope<*>.slideOutToLeft() =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(TRANSITION_DURATION)
    )

fun AnimatedContentTransitionScope<*>.slideOutToDown() =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(TRANSITION_DURATION)
    )
