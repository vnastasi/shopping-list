package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import md.vnastasi.shoppinglist.screen.additems.AddItemsViewModel
import md.vnastasi.shoppinglist.screen.additems.UiEvent

@Composable
fun AddItemsScreen(
    navController: NavController,
    viewModel: AddItemsViewModel
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val textFieldValue = rememberSaveable { mutableStateOf("") }

    val viewState = viewModel.screenState.collectAsStateWithLifecycle().value

    Column {

        TopAppBar(
            title = {
                Text("")
            },
            navigationIcon = {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    value = textFieldValue.value,
                    onValueChange = {
                        textFieldValue.value = it
                        viewModel.onUiEvent(UiEvent.OnSearchTermChanged(textFieldValue.value))
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                textFieldValue.value = ""
                                viewModel.onUiEvent(UiEvent.OnSearchTermChanged(textFieldValue.value))
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
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            itemsIndexed(items = viewState.suggestions, key = { _, it -> it.id }) { index, suggestion ->
                SuggestionRow(
                    suggestion = suggestion,
                    isLastItemInList = remember { mutableStateOf(index == viewState.suggestions.size - 1) },
                    onClick = { viewModel.onUiEvent(UiEvent.OnAddItem(suggestion.name)) }
                )
            }
        }
    }
}
