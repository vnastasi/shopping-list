package md.vnastasi.shoppinglist.screen.listdetails.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.NightMode
import com.android.resources.ScreenOrientation
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingItem
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.support.collection.crossJoin
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class ListDetailsScreenshotTest(
    config: DeviceConfig,
    private val viewState: ViewState
) {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = config,
        showSystemUi = false,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        validateAccessibility = true,
        maxPercentDifference = 1.0
    )

    @Test
    fun screenshot() {
        paparazzi.snapshot {
            AppTheme {
                ListDetailsScreen(
                    viewModel = StubListDetailsViewModelSpec(viewState),
                    navigator = StubListDetailsScreenNavigator()
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
                shoppingListId = 1L,
                shoppingListName = "Test list",
                listOfShoppingItems = persistentListOf()
            ),
            ViewState.Ready(
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
            ),
            ViewState.Loading
        )
    }
}
