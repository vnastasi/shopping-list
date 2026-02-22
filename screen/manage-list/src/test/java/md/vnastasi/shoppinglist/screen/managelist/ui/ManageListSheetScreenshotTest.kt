package md.vnastasi.shoppinglist.screen.managelist.ui

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.NightMode
import com.android.resources.ScreenOrientation
import md.vnastasi.shoppinglist.screen.managelist.model.TextValidationError
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState
import md.vnastasi.shoppinglist.support.collection.crossJoin
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class ManageListSheetScreenshotTest(
    config: DeviceConfig,
    private val viewState: Pair<String, ViewState>
) {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = config,
        showSystemUi = false,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        validateAccessibility = false,
        maxPercentDifference = 0.01
    )

    @Test
    fun screenshot() {
        paparazzi.snapshot {
            AppTheme {
                val sheetState = SheetState(
                    skipPartiallyExpanded = true,
                    skipHiddenState = true,
                    initialValue = SheetValue.Expanded,
                    confirmValueChange = { true },
                    positionalThreshold = { 1.0f },
                    velocityThreshold = { 1.0f }
                )

                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = { }
                ) {
                    ManageListSheet(
                        viewModel = StubManageListViewModel(expectedListName = viewState.first, expectedViewState = viewState.second),
                        navigator = StubManageListNavigator()
                    )
                }
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

        private fun viewStates(): Sequence<Pair<String, ViewState>> = sequenceOf(
            "" to ViewState.INIT,
            "" to ViewState(validationError = TextValidationError.EMPTY, isSaveEnabled = false, navigationTarget = null),
            " " to ViewState(validationError = TextValidationError.BLANK, isSaveEnabled = false, navigationTarget = null),
            "New list name" to ViewState(validationError = TextValidationError.NONE, isSaveEnabled = true, navigationTarget = null),
        )
    }
}
