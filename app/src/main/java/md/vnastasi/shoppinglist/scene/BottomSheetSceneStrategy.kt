package md.vnastasi.shoppinglist.scene

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

@Composable
internal fun <T : Any> rememberBottomSheetSceneStrategy(): BottomSheetSceneStrategy<T> =
    remember { BottomSheetSceneStrategy() }

@OptIn(ExperimentalMaterial3Api::class)
internal class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull()
        val bottomSheetProperties = lastEntry?.metadata?.get(BOTTOM_SHEET_KEY) as? ModalBottomSheetProperties
        return bottomSheetProperties?.let { properties ->
            @Suppress("UNCHECKED_CAST")
            BottomSheetScene(
                key = lastEntry.contentKey as T,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                entry = lastEntry,
                modalBottomSheetProperties = properties,
                onBack = onBack
            )
        }
    }

    companion object {

        @OptIn(ExperimentalMaterial3Api::class)
        fun bottomSheet(modalBottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties()): Map<String, Any> =
            mapOf(BOTTOM_SHEET_KEY to modalBottomSheetProperties)

        private const val BOTTOM_SHEET_KEY = "BottomSheetScene-BottomSheetProperties"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private class BottomSheetScene<T : Any>(
    override val key: T,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entry: NavEntry<T>,
    private val modalBottomSheetProperties: ModalBottomSheetProperties,
    private val onBack: () -> Unit,
) : OverlayScene<T> {

    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable (() -> Unit) = {
        ModalBottomSheet(
            onDismissRequest = onBack,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            properties = modalBottomSheetProperties,
        ) {
            entry.Content()
        }
    }
}
