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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import md.vnastasi.shoppinglist.support.theme.AppTypography

@Composable
internal fun SuggestionRow(
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
                .widthIn(max = AppDimensions.contentMaxWidth)
                .align(Alignment.Center)
                .clickable { onClick.invoke() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = AppDimensions.listItemMinHeight)
                    .padding(
                        start = AppDimensions.paddingLarge,
                        end = AppDimensions.paddingSmall,
                        top = AppDimensions.paddingSmall,
                        bottom = AppDimensions.paddingSmall
                    ),
                horizontalArrangement = Arrangement.Start
            ) {

                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = suggestion.name,
                    style = AppTypography.titleLarge
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                if (isDeletable) {
                    IconButton(
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.CenterVertically),
                        onClick = { onDelete.invoke() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.add_items_btn_delete_suggestion_acc)
                        )
                    }
                }
            }

            if (!isLastItemInList) {
                HorizontalDivider(
                    thickness = AppDimensions.divider
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
    AppTheme {
        SuggestionRow(
            suggestion = NameSuggestion(1L, "Name suggestion"),
            isLastItemInList = true,
            isDeletable = true,
            onClick = { },
            onDelete = { }
        )
    }
}

@Preview(
    name = "Non-deletable",
    showBackground = true,
    backgroundColor = 0xFFFFFBFE,
    widthDp = 520
)
@Composable
private fun SuggestionRowPreview2() {
    AppTheme {
        SuggestionRow(
            suggestion = NameSuggestion(1L, "Name suggestion"),
            isLastItemInList = true,
            isDeletable = false,
            onClick = { },
            onDelete = { }
        )
    }
}
