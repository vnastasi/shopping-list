package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.support.theme.AppTheme

@Composable
internal fun SearchBar(
    modifier: Modifier,
    searchTerm: MutableState<String>,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = modifier
            .indicatorLine(
                enabled = true,
                isError = false,
                interactionSource = MutableInteractionSource(),
                colors = TextFieldDefaults.colors()
            ),
        value = searchTerm.value,
        placeholder = {
            Text(text = stringResource(R.string.add_items_search_title))
        },
        onValueChange = { newValue ->
            searchTerm.value = newValue
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        trailingIcon = {
            IconButton(
                onClick = {
                    searchTerm.value = ""
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null
                )
            }
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
    )
}

@Preview(
    showBackground = true
)
@Composable
private fun SearchBarPreview() {
    AppTheme {
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            searchTerm = remember { mutableStateOf("Search term") }
        )
    }
}
