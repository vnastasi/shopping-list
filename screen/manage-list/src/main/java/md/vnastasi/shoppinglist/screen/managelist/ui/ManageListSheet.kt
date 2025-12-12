package md.vnastasi.shoppinglist.screen.managelist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.managelist.model.TextValidationError
import md.vnastasi.shoppinglist.screen.managelist.model.UiEvent
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState
import md.vnastasi.shoppinglist.screen.managelist.nav.ManageListNavigator
import md.vnastasi.shoppinglist.screen.managelist.ui.TestTags.MANAGE_LIST_TEXT_FIELD
import md.vnastasi.shoppinglist.screen.managelist.vm.ManageListViewModelSpec
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme

@Composable
fun ManageListSheet(
    viewModel: ManageListViewModelSpec,
    navigator: ManageListNavigator
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    ManageListSheet(
        viewState = viewState,
        onTextChanged = { viewModel.dispatch(UiEvent.OnNameChange(it)) },
        onSave = {
            viewModel.dispatch(UiEvent.OnSaveList(it))
            navigator.close()
        }
    )
}

@Composable
private fun ManageListSheet(
    viewState: ViewState,
    onTextChanged: (String) -> Unit,
    onSave: (String) -> Unit
) {
    val textFieldState = rememberTextFieldState(initialText = viewState.name)

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }
            .drop(1)
            .collectLatest { onTextChanged(it) }
    }

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
                .align(Alignment.CenterHorizontally)
                .testTag(MANAGE_LIST_TEXT_FIELD),
            state = textFieldState,
            label = {
                Text(
                    text = stringResource(R.string.list_form_input_title)
                )
            },
            isError = viewState.validationError != TextValidationError.NONE,
            supportingText = {
                when (viewState.validationError) {
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
            onKeyboardAction = { onSave(textFieldState.text.toString()) }
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
            onClick = { onSave(textFieldState.text.toString()) }
        ) {
            Text(
                text = stringResource(R.string.list_form_btn_save)
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun ManageListSheetPreview() {
    AppTheme {
        val sheetState = SheetState(
            skipPartiallyExpanded = true,
            skipHiddenState = true,
            initialValue = SheetValue.Expanded,
            confirmValueChange = { true },
            positionalThreshold = { 1.0f },
            velocityThreshold = { 1.0f }
        )

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { }
        ) {
            ManageListSheet(
                viewState = ViewState(name = "List", validationError = TextValidationError.NONE, isSaveEnabled = true),
                onTextChanged = { },
                onSave = { }
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun ManageListSheetWithErrorsPreview() {
    AppTheme {
        val sheetState = SheetState(
            skipPartiallyExpanded = true,
            skipHiddenState = true,
            initialValue = SheetValue.Expanded,
            confirmValueChange = { true },
            positionalThreshold = { 1.0f },
            velocityThreshold = { 1.0f }
        )

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { }
        ) {
            ManageListSheet(
                viewState = ViewState(name = " ", validationError = TextValidationError.BLANK, isSaveEnabled = true),
                onTextChanged = { },
                onSave = { }
            )
        }
    }
}
