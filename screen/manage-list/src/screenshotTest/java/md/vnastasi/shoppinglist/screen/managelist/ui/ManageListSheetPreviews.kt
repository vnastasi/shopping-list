package md.vnastasi.shoppinglist.screen.managelist.ui

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import md.vnastasi.shoppinglist.screen.managelist.model.TextValidationError
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState
import md.vnastasi.shoppinglist.support.collection.ScreenshotPreviews
import md.vnastasi.shoppinglist.support.theme.AppTheme

@PreviewTest
@ScreenshotPreviews
@Composable
fun NewListSheet() {
    ExpandedSheet(
        viewState = ViewState.INIT,
        listName = ""
    )
}

@PreviewTest
@ScreenshotPreviews
@Composable
fun EmptyListNameSheet() {
    ExpandedSheet(
        viewState = ViewState(validationError = TextValidationError.EMPTY, isSaveEnabled = false, navigationTarget = null),
        listName = " "
    )
}

@PreviewTest
@ScreenshotPreviews
@Composable
fun BlankListNameSheet() {
    ExpandedSheet(
        viewState = ViewState(validationError = TextValidationError.BLANK, isSaveEnabled = false, navigationTarget = null),
        listName = " "
    )
}

@PreviewTest
@ScreenshotPreviews
@Composable
fun ExistingListSheet() {
    ExpandedSheet(
        viewState = ViewState(validationError = TextValidationError.NONE, isSaveEnabled = true, navigationTarget = null),
        listName = "Updated list"
    )
}

@Composable
private fun ExpandedSheet(
    viewState: ViewState,
    listName: String
) {
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
                viewModel = StubManageListViewModel(
                    expectedListName = listName,
                    expectedViewState = viewState
                ),
                navigator = StubManageListNavigator()
            )
        }
    }
}