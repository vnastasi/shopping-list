package md.vnastasi.shoppinglist.support.screenshot.test

import app.cash.paparazzi.DeviceConfig
import com.android.resources.NightMode
import com.android.resources.ScreenOrientation

fun defaultDeviceConfigurations(): Sequence<DeviceConfig> = sequenceOf(
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
