package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList

@Composable
fun ShoppingItemRow(
    shoppingItem: ShoppingItem,
    isLastItemInList: State<Boolean>,
    onClick: (ShoppingItem) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .align(Alignment.Center)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick.invoke(shoppingItem) }
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = shoppingItem.isChecked,
                    onCheckedChange = null
                )

                val textStyle = TextStyle.Default.copy(
                    textDecoration = if (shoppingItem.isChecked) TextDecoration.LineThrough else TextDecoration.None
                )

                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = shoppingItem.name,
                    style = textStyle,
                    fontSize = 18.sp
                )
            }

            if (!isLastItemInList.value) {
                Divider(
                    thickness = 1.dp
                )
            }
        }
    }
}

@Preview(
    name = "Checked item",
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
fun ShoppingItemRowPreview1() {
    val shoppingItem = ShoppingItem(
        id = 1,
        name = "Sample shopping item",
        isChecked = true,
        list = ShoppingList(1, "list")
    )
    ShoppingItemRow(
        shoppingItem = shoppingItem,
        isLastItemInList = remember { mutableStateOf(true) },
        onClick = { }
    )
}

@Preview(
    name = "Unchecked item",
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
fun ShoppingItemRowPreview2() {
    val shoppingItem = ShoppingItem(
        id = 1,
        name = "Sample shopping item",
        isChecked = false,
        list = ShoppingList(1, "list")
    )
    ShoppingItemRow(
        shoppingItem = shoppingItem,
        isLastItemInList = remember { mutableStateOf(true) },
        onClick = { }
    )
}
