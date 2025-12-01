package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.flow.collectLatest
import md.vnastasi.shoppinglist.res.R

@Composable
internal fun SearchBar(
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit,
    onValueAccepted: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val searchTermTextFieldState = rememberTextFieldState(initialText = "")

    LaunchedEffect(searchTermTextFieldState) {
        snapshotFlow { searchTermTextFieldState.text.toString() }.collectLatest(onValueChanged)
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        modifier = modifier.focusRequester(focusRequester),
        state = searchTermTextFieldState,
        placeholder = {
            Text(text = stringResource(R.string.add_items_search_title))
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
                onClick = { searchTermTextFieldState.clearText() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null
                )
            }
        },
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        onKeyboardAction = KeyboardActionHandler { onValueAccepted.invoke(searchTermTextFieldState.text.toString()) }
    )
}
