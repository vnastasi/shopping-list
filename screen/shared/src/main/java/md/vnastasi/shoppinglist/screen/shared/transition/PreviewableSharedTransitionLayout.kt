package md.vnastasi.shoppinglist.screen.shared.transition

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun PreviewableSharedTransitionLayout(
    content: @Composable ExtendedSharedTransitionScope.() -> Unit
) {
    SharedTransitionLayout {
        AnimatedContent(targetState = Unit) {
            val scope = ExtendedSharedTransitionScope(
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedContentScope = this@AnimatedContent
            )
            scope.content()
        }
    }
}
