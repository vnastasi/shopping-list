package md.vnastasi.shoppinglist.screen.additems.ui

import app.cash.paparazzi.DeviceConfig
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.support.screenshot.test.ScreenshotTest
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class AddItemsScreenshotTest(config: DeviceConfig, viewState: ViewState) : ScreenshotTest<ViewState>(config, viewState) {

    @Test
    fun verifyScreenshot() {
        paparazzi.snapshot {
            AppTheme {
                AddItemsScreen(
                    viewModel = StubAddItemsViewModelSpec(viewState),
                    navigator = StubAddItemsScreenNavigator()
                )
            }
        }
    }

    companion object : ParametersProvider<ViewState> {

        @JvmStatic
        @Parameters
        override fun parameters(): List<Array<Any>> = super.parameters()

        override fun viewStates(): Sequence<ViewState> = sequenceOf(
            ViewState(
                searchTerm = "",
                suggestions = persistentListOf(),
                toastMessage = null
            ),
            ViewState(
                searchTerm = "A search term",
                suggestions = persistentListOf(
                    NameSuggestion(id = 1L, name = "Suggestion 1"),
                    NameSuggestion(id = 2L, name = "Suggestion 2"),
                    NameSuggestion(id = 3L, name = "Suggestion 3")
                ),
                toastMessage = null
            )
        )
    }
}
