package md.vnastasi.shoppinglist.screen.listoverview.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.support.ui.bottomsheet.BottomSheetBehaviour

@Composable
fun ShoppingListFormBottomSheet(
    behaviour: BottomSheetBehaviour,
    onSaveList: (String) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val textFieldValue = rememberSaveable { mutableStateOf("") }
    val textFieldError = rememberSaveable { mutableStateOf(TextFieldValidator.Error.NONE) }
    val textFieldValidator = remember { TextFieldValidator() }

    val saveAndCloseSheet: () -> Unit = {
        val validationResult = textFieldValidator.validate(textFieldValue.value)
        textFieldError.value = validationResult

        if (validationResult.noErrors()) {
            onSaveList.invoke(textFieldValue.value.trim())
            behaviour.scope.launch {
                keyboardController?.hide()
                behaviour.state.hide()
            }
        }
    }

    LaunchedEffect(behaviour.state.currentValue) {
        if (behaviour.state.currentValue == SheetValue.Hidden) {
            textFieldValue.value = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {

        OutlinedTextField(
            modifier = Modifier
                .widthIn(min = 320.dp, max = 520.dp)
                .align(Alignment.CenterHorizontally),
            value = textFieldValue.value,
            label = {
                Text(
                    text = "Shopping list name"
                )
            },
            isError = textFieldError.value.hasErrors(),
            supportingText = {
                when (textFieldError.value) {
                    TextFieldValidator.Error.NONE -> Unit
                    TextFieldValidator.Error.EMPTY -> Text(text = "Value cannot be empty")
                    TextFieldValidator.Error.BLANK -> Text(text = "Value cannot be blank")
                }
            },
            maxLines = 1,
            singleLine = true,
            onValueChange = { newValue ->
                textFieldError.value = textFieldValidator.validate(newValue)
                textFieldValue.value = newValue
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    saveAndCloseSheet.invoke()
                }
            )
        )

        Button(
            modifier = Modifier
                .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(8.dp),
            enabled = textFieldError.value.noErrors(),
            onClick = saveAndCloseSheet
        ) {
            Text(
                text = "Save"
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
fun ShoppingListFormBottomSheetPreview() {
    ShoppingListFormBottomSheet(
        behaviour = BottomSheetBehaviour(
            state = rememberStandardBottomSheetState(
                skipHiddenState = true,
                initialValue = SheetValue.Expanded
            ),
            scope = rememberCoroutineScope()
        ),
        onSaveList = { }
    )
}
