package md.vnastasi.shoppinglist.scene

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass
import md.vnastasi.shoppinglist.screen.shared.content.LocalBackButtonVisibility

private const val MIN_WIDTH_BREAKPOINT = 720
private const val MIN_HEIGHT_BREAKPOINT = WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND

private const val LIST_PANE_WEIGHT = 0.4f
private const val DETAIL_PANE_WEIGHT = 0.6f

@Composable
internal fun <T : Any> rememberListDetailSceneStrategy(): ListDetailSceneStrategy<T> {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return remember(windowSizeClass) { ListDetailSceneStrategy(windowSizeClass) }
}

internal class ListDetailSceneStrategy<T : Any>(
    private val windowSizeClass: WindowSizeClass
) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val detailEntry = entries.lastOrNull()?.takeIf { it.metadata.containsKey(DETAIL_KEY) }
        val listEntry = entries.findLast { it.metadata.containsKey(LIST_KEY) }

        return if (isScreenWideEnough() && detailEntry != null && listEntry != null) {
            ListDetailScene(
                key = listEntry.contentKey,
                previousEntries = entries.dropLast(1),
                listEntry = listEntry,
                detailEntry = detailEntry
            )
        } else {
            null
        }
    }

    private fun isScreenWideEnough() =
        windowSizeClass.isAtLeastBreakpoint(widthDpBreakpoint = MIN_WIDTH_BREAKPOINT, heightDpBreakpoint = MIN_HEIGHT_BREAKPOINT)

    companion object {

        const val LIST_KEY = "ListDetailScene-List"

        const val DETAIL_KEY = "ListDetailScene-Detail"

        fun listPane() = mapOf(LIST_KEY to true)

        fun detailPane() = mapOf(DETAIL_KEY to true)
    }
}

private class ListDetailScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    private val listEntry: NavEntry<T>,
    private val detailEntry: NavEntry<T>,
) : Scene<T> {

    override val entries: List<NavEntry<T>> = listOf(listEntry, detailEntry)

    override val content: @Composable (() -> Unit) = {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.weight(LIST_PANE_WEIGHT)
            ) {
                listEntry.Content()
            }

            CompositionLocalProvider(LocalBackButtonVisibility provides false) {
                Column(
                    modifier = Modifier.weight(DETAIL_PANE_WEIGHT)
                ) {
                    AnimatedContent(
                        targetState = detailEntry,
                        contentKey = { it.contentKey },
                        transitionSpec = {
                            slideInHorizontally(initialOffsetX = { it }) togetherWith slideOutHorizontally(targetOffsetX = { -it })
                        }
                    ) { entry ->
                        entry.Content()
                    }
                }
            }
        }
    }
}
