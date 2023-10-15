package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SuggestionRow(
    text: String,
    isLastItemInList: State<Boolean>,
    onClick: () -> Unit
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
                    .clickable { onClick.invoke() }
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = text,
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
