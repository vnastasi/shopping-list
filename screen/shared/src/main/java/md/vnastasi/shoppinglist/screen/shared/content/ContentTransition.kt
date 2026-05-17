package md.vnastasi.shoppinglist.screen.shared.content

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith

private const val EXIT_TRANSITION_DURATION = 500
private const val EXIT_TRANSITION_DELAY = 90
private const val EXIT_TRANSITION_SCALE = 0.92f

private val contentEnterTransition: EnterTransition =
    fadeIn(
        animationSpec = tween(
            durationMillis = EXIT_TRANSITION_DURATION,
            delayMillis = EXIT_TRANSITION_DELAY
        )
    ) + scaleIn(
        initialScale = EXIT_TRANSITION_SCALE,
        animationSpec = tween(
            durationMillis = EXIT_TRANSITION_DURATION,
            delayMillis = EXIT_TRANSITION_DELAY
        )
    )

private val contentExitTransition: ExitTransition =
    fadeOut(
        animationSpec = tween(
            durationMillis = EXIT_TRANSITION_DURATION
        )
    )

val contentTransitionSpec: ContentTransform =
    contentEnterTransition togetherWith contentExitTransition
