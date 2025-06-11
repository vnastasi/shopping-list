package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import md.vnastasi.shoppinglist.res.R

@Composable
internal fun SearchBar(
    modifier: Modifier = Modifier,
    searchTerm: MutableState<String>,
    onValueAccepted: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        modifier = modifier.focusRequester(focusRequester),
        value = searchTerm.value,
        placeholder = {
            Text(text = stringResource(R.string.add_items_search_title))
        },
        onValueChange = { searchTerm.value = it },
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
                onClick = { searchTerm.value = "" }
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
            onDone = { onValueAccepted.invoke() }
        ),
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
