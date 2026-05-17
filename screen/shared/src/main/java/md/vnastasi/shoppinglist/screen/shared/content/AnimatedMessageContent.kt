package md.vnastasi.shoppinglist.screen.shared.content

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import md.vnastasi.shoppinglist.support.theme.AppTypography

private const val CONTENT_WEIGHT = 0.75f
private const val SPACER_WEIGHT = 0.25f

@Composable
fun AnimatedMessageContent(
    contentPaddings: PaddingValues,
    @RawRes animationResId: Int,
    @StringRes messageResId: Int
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val isExtended = windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
    if (isExtended) {
        ExtendedContent(
            contentPaddings = contentPaddings,
            animationResId = animationResId,
            messageResId = messageResId
        )
    } else {
        CompactContent(
            contentPaddings = contentPaddings,
            messageResId = messageResId
        )
    }
}

@Composable
private fun CompactContent(
    contentPaddings: PaddingValues,
    @StringRes messageResId: Int
) {
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
            text = stringResource(messageResId),
            style = AppTypography.headlineMedium
        )
    }
}

@Composable
private fun ExtendedContent(
    contentPaddings: PaddingValues,
    @RawRes animationResId: Int,
    @StringRes messageResId: Int
) {
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
            val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(animationResId))

            if (!LocalInspectionMode.current) {
                LottieAnimation(
                    modifier = Modifier.fillMaxWidth(),
                    composition = lottieComposition,
                    iterations = LottieConstants.IterateForever,
                    enableMergePaths = true
                )
            } else {
                AnimationPlaceholder()
            }

            Text(
                modifier = Modifier
                    .padding(
                        top = AppDimensions.paddingLarge,
                        start = AppDimensions.paddingMedium,
                        end = AppDimensions.paddingMedium
                    )
                    .align(Alignment.CenterHorizontally),
                text = stringResource(messageResId),
                style = AppTypography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }

        Spacer(
            modifier = Modifier.weight(SPACER_WEIGHT)
        )
    }
}

@Composable
private fun ColumnScope.AnimationPlaceholder() {
    Box(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(192.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Animation"
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
private fun EmptyListOverviewScreenContentPreview() {
    AppTheme {
        AnimatedMessageContent(
            contentPaddings = PaddingValues(AppDimensions.paddingMedium),
            animationResId = R.raw.lottie_anim_loading,
            messageResId = R.string.list_details_loading
        )
    }
}
