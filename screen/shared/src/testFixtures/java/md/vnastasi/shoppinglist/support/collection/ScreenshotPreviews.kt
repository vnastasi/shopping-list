package md.vnastasi.shoppinglist.support.collection

import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Phone",
    device = TestDeviceConfigurations.PHONE
)
@Preview(
    name = "Phone - Landscape",
    device = TestDeviceConfigurations.PHONE_LANDSCAPE
)
@Preview(
    name = "Phone - Dark",
    device = TestDeviceConfigurations.PHONE,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL
)
@Preview(
    name = "Phone - 200% font",
    device = TestDeviceConfigurations.PHONE,
    fontScale = 2.0f
)
@Preview(
    name = "Small phone",
    device = TestDeviceConfigurations.SMALL_PHONE,
)
@Preview(
    name = "Tablet",
    device = TestDeviceConfigurations.TABLET,
)
annotation class ScreenshotPreviews
