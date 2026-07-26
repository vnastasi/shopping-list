package md.vnastasi.shoppinglist.screen.shared.transition

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Stable

sealed interface ExtendedSharedTransitionScope {

    val sharedTransitionScope: SharedTransitionScope

    val animatedContentScope: AnimatedContentScope
}

@Stable
private class ExtendedSharedTransitionScopeImpl(
    override val sharedTransitionScope: SharedTransitionScope,
    override val animatedContentScope: AnimatedContentScope
) : ExtendedSharedTransitionScope

fun ExtendedSharedTransitionScope(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
): ExtendedSharedTransitionScope =
    ExtendedSharedTransitionScopeImpl(sharedTransitionScope, animatedContentScope)
