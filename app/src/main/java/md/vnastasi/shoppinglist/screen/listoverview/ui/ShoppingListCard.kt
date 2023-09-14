package md.vnastasi.shoppinglist.screen.listoverview.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
fun ShoppingListCard(
    list: ShoppingList,
    onClickItem: (ShoppingList) -> Unit = { },
    onDeleteItem: (ShoppingList) -> Unit = { }
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .padding(16.dp)
                .align(Alignment.Center)
                .clickable { onClickItem.invoke(list) },
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
                    text = list.name
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )

                Image(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable { onDeleteItem.invoke(list) },
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(
    heightDp = 96
)
@Composable
fun ShoppingListCardPreview() {
    val shoppingList = ShoppingList(1, "Sample shopping list")
    ShoppingListCard(list = shoppingList)
}
