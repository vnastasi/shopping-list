package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import md.vnastasi.shoppinglist.domain.model.NameSuggestion

@Composable
fun SuggestionRow(
    suggestion: NameSuggestion,
    isLastItemInList: Boolean,
    isDeletable: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(0.dp)
                .widthIn(max = 520.dp)
                .align(Alignment.Center)
                .clickable { onClick.invoke() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 64.dp)
                    .padding(start = 24.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = suggestion.name,
                    fontSize = 18.sp
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                if (isDeletable) {
                    IconButton(
                        modifier = Modifier
                            .padding(0.dp)
                            .wrapContentSize()
                            .align(Alignment.CenterVertically),
                        onClick = { onDelete.invoke() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null
                        )
                    }
                }
            }

            if (!isLastItemInList) {
                Divider(
                    thickness = 1.dp
                )
            }
        }
    }
}

@Preview(
    name = "Deletable",
    showBackground = true,
    backgroundColor = 0xFFFFFBFE,
    widthDp = 520
)
@Composable
private fun SuggestionRowPreview1() {
    SuggestionRow(
        suggestion = NameSuggestion(1L, "Name suggestion"),
        isLastItemInList = true,
        isDeletable = true,
        onClick = { },
        onDelete = { }
    )
}

@Preview(
    name = "Non-deletable",
    showBackground = true,
    backgroundColor = 0xFFFFFBFE,
    widthDp = 520
)
@Composable
private fun SuggestionRowPreview2() {
    SuggestionRow(
        suggestion = NameSuggestion(1L, "Name suggestion"),
        isLastItemInList = true,
        isDeletable = false,
        onClick = { },
        onDelete = { }
    )
}
