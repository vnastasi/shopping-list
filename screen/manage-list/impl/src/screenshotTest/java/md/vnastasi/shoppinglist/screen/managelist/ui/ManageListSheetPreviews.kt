package md.vnastasi.shoppinglist.screen.managelist.ui

import androidx.compose.runtime.Composable
import com.android.tools.screenshot.PreviewTest
import md.vnastasi.shoppinglist.screen.managelist.model.TextValidationError
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.sheet.PreviewableSheetLayout
import md.vnastasi.shoppinglist.support.collection.ScreenshotPreviews
import md.vnastasi.shoppinglist.support.theme.AppTheme

@PreviewTest
@ScreenshotPreviews
@Composable
internal fun NewListSheet() {
    ExpandedSheet(
        viewState = ViewState.INIT,
        listName = ""
    )
}

@PreviewTest
@ScreenshotPreviews
@Composable
internal fun EmptyListNameSheet() {
    ExpandedSheet(
        viewState = ViewState(validationError = TextValidationError.EMPTY, isSaveEnabled = false),
        listName = " "
    )
}

@PreviewTest
@ScreenshotPreviews
@Composable
internal fun BlankListNameSheet() {
    ExpandedSheet(
        viewState = ViewState(validationError = TextValidationError.BLANK, isSaveEnabled = false),
        listName = " "
    )
}

@PreviewTest
@ScreenshotPreviews
@Composable
internal fun ExistingListSheet() {
    ExpandedSheet(
        viewState = ViewState(validationError = TextValidationError.NONE, isSaveEnabled = true),
        listName = "Updated list"
    )
}

@Composable
private fun ExpandedSheet(
    viewState: ViewState,
    listName: String
) {
    AppTheme {
        PreviewableSheetLayout {
            ManageListSheet(
                viewModel = StubManageListViewModel(
                    expectedListName = listName,
                    expectedViewState = viewState
                ),
                onNavigate = { }
            )
        }
    }
}
