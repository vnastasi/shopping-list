package md.vnastasi.shoppinglist.screen.overview.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.NightMode
import com.android.resources.ScreenOrientation
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingListDetails
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.support.async.crossJoin
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class ListOverviewScreenshotTest(
    config: DeviceConfig,
    private val viewState: ViewState
) {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = config,
        showSystemUi = false,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        validateAccessibility = false,
        maxPercentDifference = 1.0
    )

    @Test
    fun screenshot() {
        paparazzi.snapshot {
            AppTheme {
                ListOverviewScreen(
                    viewModel = StubListOverviewViewModel(viewState),
                    navigator = StubListOverviewScreenNavigator()
                )
            }
        }
    }

    companion object {

        @JvmStatic
        @Parameters
        fun parameters(): List<Array<Any>> = crossJoin(deviceConfigurations(), viewStates())
            .map { arrayOf(it.first, it.second) }
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
            ViewState.Ready(
                shoppingLists = persistentListOf()
            ),
            ViewState.Ready(
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
            ViewState.Ready(
                shoppingLists = persistentListOf(),
                navigationTarget = NavigationTarget.ShoppingListForm
            ),
            ViewState.Idle
        )
    }
}
