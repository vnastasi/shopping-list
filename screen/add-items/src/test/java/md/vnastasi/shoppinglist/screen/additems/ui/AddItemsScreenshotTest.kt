package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.runtime.CompositionLocalProvider
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.NightMode
import com.android.resources.ScreenOrientation
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.content.LocalPresentationMode
import md.vnastasi.shoppinglist.screen.shared.content.PresentationMode
import md.vnastasi.shoppinglist.support.collection.crossJoin
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class AddItemsScreenshotTest(
    config: DeviceConfig,
    private val viewState: ViewState,
    private val searchTermValue: String
) {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = config,
        showSystemUi = false,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        validateAccessibility = true,
        maxPercentDifference = 0.01
    )

    @Test
    fun fullScreen() {
        paparazzi.snapshot {
            AppTheme {
                CompositionLocalProvider(LocalPresentationMode provides PresentationMode.FullScreen) {
                    AddItemsScreen(
                        viewModel = StubAddItemsViewModelSpec(viewState = viewState, searchTermValue = searchTermValue),
                        navigator = StubAddItemsScreenNavigator()
                    )
                }
            }
        }
    }

    @Test
    fun dialog() {
        paparazzi.snapshot {
            AppTheme {
                CompositionLocalProvider(LocalPresentationMode provides PresentationMode.Dialog) {
                    AddItemsScreen(
                        viewModel = StubAddItemsViewModelSpec(viewState = viewState, searchTermValue = searchTermValue),
                        navigator = StubAddItemsScreenNavigator()
                    )
                }
            }
        }
    }

    companion object {

        @JvmStatic
        @Parameters
        fun parameters(): List<Array<Any>> = crossJoin(deviceConfigurations(), viewStates(), searchTermValues())
            .map { arrayOf(it.first, it.second, it.third) }
            .toList()

        private fun deviceConfigurations(): Sequence<DeviceConfig> = sequenceOf(
            DeviceConfig.PIXEL_6.copy(
                orientation = ScreenOrientation.PORTRAIT,
                nightMode = NightMode.NOTNIGHT,
                softButtons = false,
            ),
            DeviceConfig.PIXEL_6.copy(
                orientation = ScreenOrientation.PORTRAIT,
                nightMode = NightMode.NIGHT,
                softButtons = false
            ),
            DeviceConfig.PIXEL_6.copy(
                orientation = ScreenOrientation.LANDSCAPE,
                nightMode = NightMode.NOTNIGHT,
                softButtons = false
            ),
            DeviceConfig.PIXEL_6.copy(
                orientation = ScreenOrientation.LANDSCAPE,
                nightMode = NightMode.NIGHT,
                softButtons = false
            ),
            DeviceConfig.PIXEL_C.copy(
                orientation = ScreenOrientation.PORTRAIT,
                nightMode = NightMode.NOTNIGHT,
                softButtons = false,
            ),
            DeviceConfig.PIXEL_C.copy(
                orientation = ScreenOrientation.PORTRAIT,
                nightMode = NightMode.NIGHT,
                softButtons = false
            ),
            DeviceConfig.PIXEL_C.copy(
                orientation = ScreenOrientation.LANDSCAPE,
                nightMode = NightMode.NOTNIGHT,
                softButtons = false
            ),
            DeviceConfig.PIXEL_C.copy(
                orientation = ScreenOrientation.LANDSCAPE,
                nightMode = NightMode.NIGHT,
                softButtons = false
            )
        )

        private fun viewStates(): Sequence<ViewState> = sequenceOf(
            ViewState(
                suggestions = persistentListOf(),
                toastMessage = null,
                navigationTarget = null
            ),
            ViewState(
                suggestions = persistentListOf(
                    NameSuggestion(id = 1L, name = "Suggestion 1"),
                    NameSuggestion(id = 2L, name = "Suggestion 2"),
                    NameSuggestion(id = 3L, name = "Suggestion 3")
                ),
                toastMessage = null,
                navigationTarget = null
            )
        )

        private fun searchTermValues(): Sequence<String> = sequenceOf(
            "",
            "A search term"
        )
    }
}
