package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.window.core.layout.WindowHeightSizeClass
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import md.vnastasi.shoppinglist.support.theme.AppTypography

private const val CONTENT_WEIGHT = 0.75f
private const val SPACER_WEIGHT = 0.25f

@Composable
internal fun EmptyListDetailsScreenContent(
    contentPaddings: PaddingValues
) {
    val windowHeightSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowHeightSizeClass

    if (windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
        CompactContent(contentPaddings)
    } else {
        ExtendedContent(contentPaddings)
    }
}

@Composable
private fun CompactContent(contentPaddings: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = contentPaddings.calculateBottomPadding()
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.list_details_empty),
            style = AppTypography.headlineMedium
        )
    }
}

@Composable
private fun ExtendedContent(contentPaddings: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = contentPaddings.calculateTopPadding(),
                bottom = contentPaddings.calculateBottomPadding()
            )
    ) {
        Column(
            modifier = Modifier
                .weight(CONTENT_WEIGHT)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_anim_empty_box))

            LottieAnimation(
                modifier = Modifier.fillMaxWidth(),
                composition = composition,
                iterations = 1
            )

            Text(
                modifier = Modifier
                    .padding(
                        top = AppDimensions.paddingLarge,
                        start = AppDimensions.paddingMedium,
                        end = AppDimensions.paddingMedium
                    )
                    .align(Alignment.CenterHorizontally),
                text = stringResource(R.string.list_details_empty),
                style = AppTypography.headlineMedium
            )
        }

        Spacer(
            modifier = Modifier.weight(SPACER_WEIGHT)
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
private fun EmptyListDetailsScreenContentPreview() {
    AppTheme {
        EmptyListDetailsScreenContent(
            contentPaddings = PaddingValues(AppDimensions.paddingMedium)
        )
    }
}
