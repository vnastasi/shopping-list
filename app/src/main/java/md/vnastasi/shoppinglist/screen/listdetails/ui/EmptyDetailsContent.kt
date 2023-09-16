package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun EmptyDetailsContent(
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
            text = "Shopping list is empty",
            fontSize = 24.sp
        )
    }
}
