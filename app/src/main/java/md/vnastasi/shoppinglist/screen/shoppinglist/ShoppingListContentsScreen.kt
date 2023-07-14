package md.vnastasi.shoppinglist.screen.shoppinglist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShoppingListContentsScreen(shoppingListId: Long) {

    Text(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        text = "This is the shoppingList $shoppingListId"
    )
}
