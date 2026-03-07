package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import com.android.tools.screenshot.PreviewTest
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.support.theme.AppTheme

@PreviewTest
@ScreenshotPreviews
@Composable
fun EmptyOverviewScreen() {
    val viewState = ViewState.Empty(
        navigationTarget = null
    )

    AppTheme {
        OverviewScreen(
            viewModel = StubOverviewViewModel(viewState),
            navigator = StubOverviewScreenNavigator()
        )
    }
}

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(
    name = "Phone",
    device = "spec:width=411dp,height=923dp,dpi=420,orientation=portrait"
)
@Preview(
    name = "Phone - Landscape",
    device = "spec:width=411dp,height=923dp,dpi=420,orientation=landscape"
)
@Preview(
    name = "Phone - Dark",
    device = "spec:width=411dp,height=923dp,dpi=420,orientation=portrait",
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL
)
@Preview(
    name = "Phone - 200% font",
    device = "spec:width=411dp,height=923dp,dpi=420,orientation=portrait",
    fontScale = 2.0f
)
@Preview(
    name = "Phone - Green",
    device = "spec:width=411dp,height=923dp,dpi=420,orientation=portrait",
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE
)
@Preview(
    name = "Phone - Red",
    device = "spec:width=411dp,height=923dp,dpi=420,orientation=portrait",
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Preview(
    name = "Small phone",
    device = "spec:width=320dp,height=533dp,dpi=240,orientation=portrait",
)
@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=900dp,dpi=420,orientation=landscape",
)
annotation class ScreenshotPreviews
