package md.vnastasi.shoppinglist.scene

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass

private const val MIN_WIDTH_BREAKPOINT = 720
private const val MIN_HEIGHT_BREAKPOINT = WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND

@Composable
internal fun <T : Any> rememberDialogWhenLargeSceneStrategy(): DialogWhenLargeSceneStrategy<T> {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return remember(windowSizeClass) { DialogWhenLargeSceneStrategy(windowSizeClass) }
}

internal class DialogWhenLargeScene<T : Any>(
    override val key: Any,
    private val entry: NavEntry<T>,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val dialogProperties: DialogProperties,
    private val onBack: () -> Unit,
) : OverlayScene<T> {

    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable (() -> Unit) = {
        Dialog(onDismissRequest = onBack, properties = dialogProperties) { entry.Content() }
    }
}

internal class DialogWhenLargeSceneStrategy<T : Any>(
    private val windowSizeClass: WindowSizeClass
) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        if (!isScreenWideEnough()) return null

        val lastEntry = entries.lastOrNull()
        val dialogProperties = lastEntry?.metadata?.get(DIALOG_WHEN_LARGE_KEY) as? DialogProperties

        return dialogProperties?.let { properties ->
            DialogWhenLargeScene(
                key = lastEntry.contentKey,
                entry = lastEntry,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                dialogProperties = properties,
                onBack = onBack,
            )
        }
    }

    private fun isScreenWideEnough() =
        windowSizeClass.isAtLeastBreakpoint(widthDpBreakpoint = MIN_WIDTH_BREAKPOINT, heightDpBreakpoint = MIN_HEIGHT_BREAKPOINT)

    companion object {

        const val DIALOG_WHEN_LARGE_KEY = "dialog-when-large"

        fun dialogWhenLarge(dialogProperties: DialogProperties = DialogProperties()): Map<String, Any> =
            mapOf(DIALOG_WHEN_LARGE_KEY to dialogProperties)
    }
}
