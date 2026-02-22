package md.vnastasi.shoppinglist.support.collection

import app.cash.paparazzi.DeviceConfig
import com.android.resources.NightMode

val DeviceConfig.displayName: String
    get() = "[size=${size.name},density=${density.dpiValue},orientation=${orientation.name},ui-mode=${nightMode.displayName},font-scale=${fontScale}]"

private val NightMode.displayName: String
    get() = when (this) {
        NightMode.NOTNIGHT -> "LIGHT"
        NightMode.NIGHT -> "DARK"
    }
