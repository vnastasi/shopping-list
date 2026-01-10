package md.vnastasi.shoppinglist.screen.additems.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.BACK_BUTTON
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SEARCH_BAR
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SUGGESTIONS_ITEM
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SUGGESTIONS_LIST
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme

private const val MAX_HEIGHT = 800
private const val MIN_HEIGHT = 640

@Composable
internal fun AddItemsDialog(
    searchTermTextFieldState: TextFieldState,
    viewState: State<ViewState>,
    onItemAddedToList: (String) -> Unit,
    onSuggestionDeleted: (NameSuggestion) -> Unit,
    onDone: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = optimalDialogHeight()),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppDimensions.paddingLarge)
                    .testTag(SEARCH_BAR),
                state = searchTermTextFieldState,
                placeholder = {
                    Text(text = stringResource(R.string.add_items_search_title))
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { searchTermTextFieldState.clearText() }
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Close,
                            contentDescription = null
                        )
                    }
                },
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                onKeyboardAction = KeyboardActionHandler {
                    onItemAddedToList.invoke(searchTermTextFieldState.text.toString())
                    searchTermTextFieldState.clearText()
                }
            )

            HorizontalDivider()

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .testTag(SUGGESTIONS_LIST)
            ) {
                itemsIndexed(
                    items = viewState.value.suggestions,
                    key = { _, suggestion -> suggestion.id }
                ) { index, suggestion ->
                    SuggestionRow(
                        modifier = Modifier
                            .animateItem()
                            .testTag(SUGGESTIONS_ITEM),
                        suggestion = suggestion,
                        isLastItemInList = index == viewState.value.suggestions.size - 1,
                        isDeletable = suggestion.id != -1L,
                        onClick = { onItemAddedToList(suggestion.name) },
                        onDelete = { onSuggestionDeleted(suggestion) }
                    )
                }
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    modifier = Modifier
                        .padding(AppDimensions.paddingMedium)
                        .testTag(BACK_BUTTON),
                    onClick = onDone
                ) {
                    Text(
                        text = "Done"
                    )
                }
            }
        }
    }
}

@Composable
private fun optimalDialogHeight(): Dp {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val fontScale = configuration.fontScale

    return remember(configuration) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            (MAX_HEIGHT * fontScale).dp
        } else {
            (MIN_HEIGHT * fontScale).dp
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun AddItemsDialogPreview() {
    val viewState = ViewState(
        suggestions = persistentListOf(
            NameSuggestion(id = -1L, "Sample item"),
            NameSuggestion(id = 1L, "Item 1"),
            NameSuggestion(id = 2L, "Item 2"),
            NameSuggestion(id = 3L, "Item 3"),
            NameSuggestion(id = 4L, "Item 4"),
            NameSuggestion(id = 5L, "Item 5"),
            NameSuggestion(id = 6L, "Item 6"),
            NameSuggestion(id = 7L, "Item 7")
        ),
        toastMessage = null
    )

    AppTheme {
        Dialog(
            onDismissRequest = { }
        ) {
            AddItemsDialog(
                searchTermTextFieldState = rememberTextFieldState(),
                viewState = remember { mutableStateOf(viewState) },
                onDone = { },
                onItemAddedToList = { },
                onSuggestionDeleted = { },
            )
        }
    }
}
