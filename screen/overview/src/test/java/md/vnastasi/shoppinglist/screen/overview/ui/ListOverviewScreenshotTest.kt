package md.vnastasi.shoppinglist.screen.overview.ui

import app.cash.paparazzi.DeviceConfig
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.TestData.createShoppingListDetails
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.support.screenshot.test.ScreenshotTest
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class ListOverviewScreenshotTest(config: DeviceConfig, viewState: ViewState) : ScreenshotTest<ViewState>(config, viewState) {

    @Test
    fun verifyScreenshot() {
        paparazzi.snapshot {
            AppTheme {
                ListOverviewScreen(
                    viewModel = StubListOverviewViewModel(viewState),
                    navigator = StubListOverviewScreenNavigator()
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
                shoppingLists = persistentListOf()
            ),
            ViewState(
                shoppingLists = persistentListOf(
                    createShoppingListDetails {
                        id = 1L
                        name = "Test list 1"
                        totalItems = 11L
                        checkedItems = 4L
                    },
                    createShoppingListDetails {
                        id = 2L
                        name = "Test list 2"
                        totalItems = 1L
                        checkedItems = 0L
                    }
                )
            ),
            ViewState(
                shoppingLists = persistentListOf(),
                navigationTarget = NavigationTarget.ShoppingListForm
            )
        )
    }
}
