package md.vnastasi.shoppinglist.screen.overview.ui

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
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import md.vnastasi.shoppinglist.support.theme.AppTypography

@Composable
internal fun EmptyListOverviewScreenContent(
    contentPaddings: PaddingValues
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowHeightSizeClass

    if (windowSizeClass == WindowHeightSizeClass.COMPACT) {
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
            )
    ) {
        Text(
            modifier = Modifier
                .padding(
                    start = AppDimensions.paddingMedium,
                    end = AppDimensions.paddingMedium
                )
                .align(Alignment.Center),
            text = stringResource(R.string.overview_empty_list),
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
                .weight(0.75f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_anim_shopping_cart))

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
                text = stringResource(R.string.overview_empty_list),
                style = AppTypography.headlineMedium
            )

        }

        Spacer(
            modifier = Modifier.weight(0.25f)
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
private fun EmptyListOverviewScreenContentPreview() {
    AppTheme {
        EmptyListOverviewScreenContent(
            contentPaddings = PaddingValues(AppDimensions.paddingMedium)
        )
    }
}
