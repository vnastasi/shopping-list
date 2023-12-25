package md.vnastasi.shoppinglist.screen.listoverview.ui

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import md.vnastasi.shoppinglist.R
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import md.vnastasi.shoppinglist.support.theme.AppTypography

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
                .widthIn(max = AppDimensions.contentMaxWidth)
                .padding(AppDimensions.paddingMedium)
                .align(Alignment.Center),
            shape = CardDefaults.outlinedShape
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickItem.invoke(list) }
                    .padding(start = AppDimensions.paddingMedium),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = list.name,
                    style = AppTypography.titleLarge
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )

                IconButton(
                    modifier = Modifier.fillMaxHeight(),
                    onClick = { onDeleteItem.invoke(list) }
                ) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.overview_btn_delete_list_acc)
                    )
                }
            }
        }
    }
}

@Preview(
    heightDp = 96
)
@Composable
private fun ShoppingListCardPreview() {
    val shoppingList = ShoppingList(1, "Sample shopping list")

    AppTheme {
        ShoppingListCard(list = shoppingList)
    }
}
