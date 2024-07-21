package md.vnastasi.shoppinglist.support.screenshot.test

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule

abstract class ScreenshotTest<V>(
    config: DeviceConfig,
    protected val viewState: V
) {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = config,
        showSystemUi = false,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        validateAccessibility = true,
        maxPercentDifference = 1.0
    )

    interface ParametersProvider<V> {

        fun parameters(): List<Array<Any>> = crossJoin(deviceConfigurations(), viewStates()).map { arrayOf(it.first, it.second) }.toList()

        fun deviceConfigurations(): Sequence<DeviceConfig> = defaultDeviceConfigurations()

        fun viewStates(): Sequence<V & Any>
    }
}
