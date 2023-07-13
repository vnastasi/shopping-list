package md.vnastasi.shoppinglist.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import md.vnastasi.shoppinglist.domain.model.ShoppingList

@Composable
fun ShoppingListItem(
    shoppingList: ShoppingList,
    onClickItem: (ShoppingList) -> Unit = { },
    onDeleteItem: (ShoppingList) -> Unit = { }
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClickItem.invoke(shoppingList) },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = shoppingList.name
            )

            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )

            Image(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { onDeleteItem.invoke(shoppingList) },
                imageVector = Icons.Default.Delete, contentDescription = null
            )
        }
    }
}

@Preview(
    heightDp = 128
)
@Composable
fun ShoppingListItemPreview() {
    val shoppingList = ShoppingList(1, "Sample shopping list")
    ShoppingListItem(shoppingList = shoppingList)
}