package md.vnastasi.shoppinglist.screen.listdetails.ui

import app.cash.paparazzi.DeviceConfig
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.TestData.createShoppingItem
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.support.screenshot.test.ScreenshotTest
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class ListDetailsScreenshotTest(config: DeviceConfig, viewState: ViewState) : ScreenshotTest<ViewState>(config, viewState) {

    @Test
    fun verifyScreenshot() {
        paparazzi.snapshot {
            AppTheme {
                ListDetailsScreen(
                    viewModel = StubListDetailsViewModelSpec(viewState),
                    navigator = StubListDetailsScreenNavigator()
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
                shoppingListId = 1L,
                shoppingListName = "Test list",
                listOfShoppingItems = persistentListOf()
            ),
            ViewState(
                shoppingListId = 1L,
                shoppingListName = "Test list",
                listOfShoppingItems = persistentListOf(
                    createShoppingItem {
                        id = 1L
                        name = "Item 1"
                        isChecked = false
                    },
                    createShoppingItem {
                        id = 2L
                        name = "Item 2"
                        isChecked = true
                    }
                )
            )
        )
    }
}
