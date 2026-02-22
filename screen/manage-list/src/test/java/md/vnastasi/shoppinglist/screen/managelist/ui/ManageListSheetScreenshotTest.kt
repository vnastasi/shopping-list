package md.vnastasi.shoppinglist.screen.managelist.ui

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.Density
import com.android.resources.NightMode
import com.android.resources.ScreenOrientation
import md.vnastasi.shoppinglist.screen.managelist.model.TextValidationError
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState
import md.vnastasi.shoppinglist.support.collection.displayName
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class ManageListSheetScreenshotTest(
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
    fun newList() {
        val viewState = ViewState.INIT
        val listName = ""
        snapshotFor(listName, viewState)
    }

    @Test
    fun emptyName() {
        val viewState = ViewState(validationError = TextValidationError.EMPTY, isSaveEnabled = false, navigationTarget = null)
        val listName = " "
        snapshotFor(listName, viewState)
    }

    @Test
    fun blankName() {
        val viewState = ViewState(validationError = TextValidationError.BLANK, isSaveEnabled = false, navigationTarget = null)
        val listName = " "
        snapshotFor(listName, viewState)
    }

    @Test
    fun updatedList() {
        val viewState = ViewState(validationError = TextValidationError.NONE, isSaveEnabled = true, navigationTarget = null)
        val listName = "Updated list"
        snapshotFor(listName, viewState)
    }

    private fun snapshotFor(listName: String, viewState: ViewState) {
        paparazzi.snapshot(config.displayName) {
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
                        viewModel = StubManageListViewModel(expectedListName = listName, expectedViewState = viewState),
                        navigator = StubManageListNavigator()
                    )
                }
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
