package md.vnastasi.shoppinglist.screen.listdetails

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListDetailsScreen(
    viewModel: ListDetailsViewModel
) {

    when(val screenState = viewModel.screenState.collectAsState().value) {
        is ScreenState.Loading -> Unit
        is ScreenState.Details -> {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "This is the shoppingList ${screenState.name}"
            )
        }
    }
}
