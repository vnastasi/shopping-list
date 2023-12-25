package md.vnastasi.shoppinglist.screen.listoverview.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import md.vnastasi.shoppinglist.support.theme.AppTypography

@Composable
fun EmptyListOverviewScreenContent(
    contentPaddings: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = contentPaddings.calculateTopPadding(),
                bottom = contentPaddings.calculateBottomPadding()
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.overview_empty_list),
            style = AppTypography.headlineMedium
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
