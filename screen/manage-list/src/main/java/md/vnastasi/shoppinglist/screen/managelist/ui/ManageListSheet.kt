package md.vnastasi.shoppinglist.screen.managelist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.managelist.model.TextValidationError
import md.vnastasi.shoppinglist.screen.managelist.model.UiEvent
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState
import md.vnastasi.shoppinglist.screen.managelist.nav.ManageListNavigator
import md.vnastasi.shoppinglist.screen.managelist.vm.ManageListViewModelSpec
import md.vnastasi.shoppinglist.support.theme.AppDimensions

@Composable
fun ManageListSheet(
    viewModel: ManageListViewModelSpec,
    navigator: ManageListNavigator
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    ManageListSheet(
        viewState = viewState,
        textFieldState = viewModel.listNameTextFieldState,
        onSave = {
            viewModel.dispatch(UiEvent.Saved)
            navigator.close()
        }
    )
}

@Composable
private fun ManageListSheet(
    viewState: ViewState,
    textFieldState: TextFieldState,
    onSave: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(AppDimensions.paddingMedium)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .widthIn(
                    min = AppDimensions.contentMinWidth,
                    max = AppDimensions.contentMaxWidth
                )
                .align(Alignment.CenterHorizontally),
            state = textFieldState,
            label = {
                Text(
                    text = stringResource(R.string.list_form_input_title)
                )
            },
            isError = viewState.textValidationError != TextValidationError.NONE,
            supportingText = {
                when (viewState.textValidationError) {
                    TextValidationError.NONE -> Unit
                    TextValidationError.EMPTY -> Text(text = stringResource(R.string.list_form_input_error_empty))
                    TextValidationError.BLANK -> Text(text = stringResource(R.string.list_form_input_error_blank))
                }
            },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            onKeyboardAction = { onSave() }
        )

        Button(
            modifier = Modifier
                .padding(
                    top = AppDimensions.paddingLarge,
                    start = AppDimensions.paddingMedium,
                    end = AppDimensions.paddingMedium
                )
                .align(Alignment.CenterHorizontally),
            enabled = viewState.isSaveEnabled,
            onClick = onSave
        ) {
            Text(
                text = stringResource(R.string.list_form_btn_save)
            )
        }
    }
}
