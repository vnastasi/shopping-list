package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import md.vnastasi.shoppinglist.support.ui.bottomsheet.BottomSheetBehaviour

@Composable
internal fun ShoppingListFormBottomSheet(
    modifier: Modifier = Modifier,
    behaviour: BottomSheetBehaviour,
    onShoppingListSaved: (String) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val textFieldValue = rememberSaveable { mutableStateOf("") }
    val textFieldError = rememberSaveable { mutableStateOf(TextFieldValidator.Error.NONE) }
    val textFieldValidator = remember { TextFieldValidator() }

    val saveAndCloseSheet: () -> Unit = {
        val validationResult = textFieldValidator.validate(textFieldValue.value)
        textFieldError.value = validationResult

        if (validationResult.noErrors()) {
            onShoppingListSaved.invoke(textFieldValue.value.trim())
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
        modifier = modifier
            .fillMaxWidth()
            .padding(AppDimensions.paddingMedium)
    ) {

        OutlinedTextField(
            modifier = Modifier
                .widthIn(
                    min = AppDimensions.contentMinWidth,
                    max = AppDimensions.contentMaxWidth
                )
                .align(Alignment.CenterHorizontally),
            value = textFieldValue.value,
            label = {
                Text(
                    text = stringResource(R.string.list_form_input_title)
                )
            },
            isError = textFieldError.value.hasErrors(),
            supportingText = {
                when (textFieldError.value) {
                    TextFieldValidator.Error.NONE -> Unit
                    TextFieldValidator.Error.EMPTY -> Text(text = stringResource(R.string.list_form_input_error_empty))
                    TextFieldValidator.Error.BLANK -> Text(text = stringResource(R.string.list_form_input_error_blank))
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
                .padding(
                    top = AppDimensions.paddingExtraLarge,
                    start = AppDimensions.paddingMedium,
                    end = AppDimensions.paddingMedium
                )
                .align(Alignment.CenterHorizontally),
            enabled = textFieldError.value.noErrors(),
            onClick = saveAndCloseSheet
        ) {
            Text(
                text = stringResource(R.string.list_form_btn_save)
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
private fun ShoppingListFormBottomSheetPreview() {
    AppTheme {
        ShoppingListFormBottomSheet(
            behaviour = BottomSheetBehaviour(
                state = rememberStandardBottomSheetState(
                    skipHiddenState = true,
                    initialValue = SheetValue.Expanded
                ),
                scope = rememberCoroutineScope()
            ),
            onShoppingListSaved = { }
        )
    }
}
