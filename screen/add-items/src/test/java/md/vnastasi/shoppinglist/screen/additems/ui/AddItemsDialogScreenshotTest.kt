package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Dialog
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.Density
import com.android.resources.NightMode
import com.android.resources.ScreenOrientation
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.content.LocalPresentationMode
import md.vnastasi.shoppinglist.screen.shared.content.PresentationMode
import md.vnastasi.shoppinglist.support.collection.displayName
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class AddItemsDialogScreenshotTest(
    private val config: DeviceConfig
) {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = config,
        showSystemUi = false,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        maxPercentDifference = 0.01
    )

    @Test
    fun empty() {
        val viewState = ViewState(
            suggestions = persistentListOf(),
            toastMessage = null,
            navigationTarget = null
        )
        val searchTermValue = ""
        snapshotFor(viewState, searchTermValue)
    }

    @Test
    fun suggestionsAvailable() {
        val viewState = ViewState(
            suggestions = persistentListOf(
                NameSuggestion(id = 1L, name = "Suggestion 1"),
                NameSuggestion(id = 2L, name = "Suggestion 2"),
                NameSuggestion(id = 3L, name = "Suggestion 3")
            ),
            toastMessage = null,
            navigationTarget = null
        )
        val searchTermValue = "Suggest"
        snapshotFor(viewState, searchTermValue)
    }

    private fun snapshotFor(viewState: ViewState, searchTermValue: String) {
        paparazzi.snapshot(config.displayName) {
            AppTheme {
                CompositionLocalProvider(LocalPresentationMode provides PresentationMode.Dialog) {
                    Dialog(
                        onDismissRequest = { }
                    ) {
                        AddItemsScreen(
                            viewModel = StubAddItemsViewModelSpec(viewState = viewState, searchTermValue = searchTermValue),
                            navigator = StubAddItemsScreenNavigator()
                        )
                    }
                }
            }
        }
    }

    companion object {

        @JvmStatic
        @Parameters
        fun parameters(): List<Array<DeviceConfig>> = deviceConfigurations().map { arrayOf(it) }.toList()

        private fun deviceConfigurations(): Sequence<DeviceConfig> = sequenceOf(
            DeviceConfig.PIXEL_C.copy(
                orientation = ScreenOrientation.PORTRAIT,
                nightMode = NightMode.NOTNIGHT,
                softButtons = false,
            ),
            DeviceConfig.PIXEL_C.copy(
                orientation = ScreenOrientation.PORTRAIT,
                nightMode = NightMode.NIGHT,
                softButtons = false,
            ),
            DeviceConfig.PIXEL_C.copy(
                orientation = ScreenOrientation.PORTRAIT,
                nightMode = NightMode.NOTNIGHT,
                softButtons = false,
                fontScale = 2.0f
            ),
            DeviceConfig.PIXEL_C.copy(
                orientation = ScreenOrientation.PORTRAIT,
                nightMode = NightMode.NOTNIGHT,
                softButtons = false,
                density = Density.MEDIUM
            ),
            DeviceConfig.PIXEL_C.copy(
                orientation = ScreenOrientation.LANDSCAPE,
                nightMode = NightMode.NOTNIGHT,
                softButtons = false,
            )
        )
    }
}
