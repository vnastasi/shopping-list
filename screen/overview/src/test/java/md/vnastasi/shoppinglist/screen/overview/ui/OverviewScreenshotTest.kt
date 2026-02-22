package md.vnastasi.shoppinglist.screen.overview.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.Density
import com.android.resources.NightMode
import com.android.resources.ScreenOrientation
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingListDetails
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.support.collection.displayName
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
internal class OverviewScreenshotTest(
    private val config: DeviceConfig,
) {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = config,
        showSystemUi = false,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        maxPercentDifference = 0.01
    )

    @Test
    fun loading() {
        snapshotFor(ViewState.Loading)
    }

    @Test
    fun empty() {
        val viewState = ViewState.Ready(
            data = persistentListOf(),
            navigationTarget = null
        )
        snapshotFor(viewState)
    }

    @Test
    fun nonEmpty() {
        val viewStateReady = ViewState.Ready(
            data = persistentListOf(
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
            ),
            navigationTarget = null
        )
        snapshotFor(viewStateReady)
    }

    private fun snapshotFor(viewState: ViewState) {
        paparazzi.snapshot(config.displayName) {
            AppTheme {
                OverviewScreen(
                    viewModel = StubOverviewViewModel(viewState),
                    navigator = StubOverviewScreenNavigator()
                )
            }
        }
    }

    companion object {

        @JvmStatic
        @Parameters
        fun parameters(): List<Array<DeviceConfig>> = deviceConfigurations().map { arrayOf(it) }.toList()

        private fun deviceConfigurations(): Sequence<DeviceConfig> = sequenceOf(
            DeviceConfig.PIXEL_9.copy(
                orientation = ScreenOrientation.PORTRAIT,
                nightMode = NightMode.NOTNIGHT,
                softButtons = false,
            ),
            DeviceConfig.PIXEL_9.copy(
                orientation = ScreenOrientation.PORTRAIT,
                nightMode = NightMode.NIGHT,
                softButtons = false,
            ),
            DeviceConfig.PIXEL_9.copy(
                orientation = ScreenOrientation.PORTRAIT,
                nightMode = NightMode.NOTNIGHT,
                softButtons = false,
                fontScale = 2.0f
            ),
            DeviceConfig.PIXEL_9.copy(
                orientation = ScreenOrientation.PORTRAIT,
                nightMode = NightMode.NOTNIGHT,
                softButtons = false,
                density = Density.MEDIUM
            ),
            DeviceConfig.PIXEL_9.copy(
                orientation = ScreenOrientation.LANDSCAPE,
                nightMode = NightMode.NOTNIGHT,
                softButtons = false,
            ),
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
