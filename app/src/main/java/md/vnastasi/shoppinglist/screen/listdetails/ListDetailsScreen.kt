package md.vnastasi.shoppinglist.screen.listdetails

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import md.vnastasi.shoppinglist.support.state.ScreenState

@Composable
fun ListDetailsScreen(
    viewModel: ListDetailsViewModel
) {

    val collectedScreenState = viewModel.screenState.collectAsState()

    when(val screenState = collectedScreenState.value) {
        is ScreenState.Loading -> Unit
        is ScreenState.Empty -> Unit
        is ScreenState.Ready -> {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "This is the shoppingList ${screenState.data.name}"
            )
        }
        is ScreenState.Failure -> Unit
    }
}
