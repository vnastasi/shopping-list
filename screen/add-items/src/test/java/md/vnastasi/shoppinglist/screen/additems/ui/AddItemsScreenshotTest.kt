package md.vnastasi.shoppinglist.screen.additems.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.NightMode
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class AddItemsScreenshotTest(
    private val screenshotName: String,
    private val viewState: ViewState,
    private val nightMode: NightMode
) {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        showSystemUi = false,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        validateAccessibility = false,
        maxPercentDifference = 1.0
    )

    @Test
    fun screenshot() {
        paparazzi.snapshot(screenshotName) {
            AppTheme(useDarkTheme = nightMode == NightMode.NIGHT) {
                AddItemsScreen(
                    viewModel = StubAddItemsViewModelSpec(viewState),
                    navigator = StubAddItemsScreenNavigator()
                )
            }
        }
    }

    companion object {

        @JvmStatic
        @Parameters
        fun parameters(): List<Array<Any>> = listOf(
            arguments {
                screenshotName = "add_items_empty_dark"
                viewState = ViewState(
                    searchTerm = "",
                    suggestions = persistentListOf(),
                    toastMessage = null
                )
                nightMode = NightMode.NIGHT
            },
            arguments {
                screenshotName = "add_items_empty_light"
                viewState = ViewState(
                    searchTerm = "",
                    suggestions = persistentListOf(),
                    toastMessage = null
                )
                nightMode = NightMode.NOTNIGHT
            },
            arguments {
                screenshotName = "add_items_non_empty_dark"
                viewState = ViewState(
                    searchTerm = "Searching...",
                    suggestions = persistentListOf(
                        NameSuggestion(-1L, "New suggestion"),
                        NameSuggestion(1L, "Old suggestion 1"),
                        NameSuggestion(2L, "Old suggestion 2")
                    ),
                    toastMessage = null
                )
                nightMode = NightMode.NIGHT
            },
            arguments {
                screenshotName = "add_items_non_empty_light"
                viewState = ViewState(
                    searchTerm = "Searching...",
                    suggestions = persistentListOf(
                        NameSuggestion(-1L, "New suggestion"),
                        NameSuggestion(1L, "Old suggestion 1"),
                        NameSuggestion(2L, "Old suggestion 2")
                    ),
                    toastMessage = null
                )
                nightMode = NightMode.NOTNIGHT
            }
        )
    }
}

private class TestArguments {

    lateinit var screenshotName: String
    lateinit var viewState: ViewState
    lateinit var nightMode: NightMode

    fun toArray(): Array<Any> = arrayOf(screenshotName, viewState, nightMode)
}

private fun arguments(block: TestArguments.() -> Unit) = TestArguments().apply(block).toArray()
