package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Dialog
import com.android.tools.screenshot.PreviewTest
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.content.LocalPresentationMode
import md.vnastasi.shoppinglist.screen.shared.content.PresentationMode
import md.vnastasi.shoppinglist.support.collection.ScreenshotPreviews
import md.vnastasi.shoppinglist.support.theme.AppTheme

@PreviewTest
@ScreenshotPreviews
@Composable
fun NoSuggestionsDialog() {
    ApplicationDialogPreview(
        viewState = ViewState(
            suggestions = persistentListOf(),
            toastMessage = null,
            navigationTarget = null
        ),
        searchTermValue = ""
    )
}

@PreviewTest
@ScreenshotPreviews
@Composable
fun SuggestionsAvailableDialog() {
    ApplicationDialogPreview(
        viewState = ViewState(
            suggestions = persistentListOf(
                NameSuggestion(id = 1L, name = "Suggestion 1"),
                NameSuggestion(id = 2L, name = "Suggestion 2"),
                NameSuggestion(id = 3L, name = "Suggestion 3")
            ),
            toastMessage = null,
            navigationTarget = null
        ),
        searchTermValue = "Suggest"
    )
}

@Composable
private fun ApplicationDialogPreview(
    viewState: ViewState,
    searchTermValue: String
) {
    AppTheme {
        CompositionLocalProvider(LocalPresentationMode provides PresentationMode.Dialog) {
            Dialog(
                onDismissRequest = { }
            ) {
                AddItemsScreen(
                    viewModel = StubAddItemsViewModelSpec(viewState = viewState, searchTermValue = searchTermValue),
                    navigator = StubAddItemsScreenNavigator()
                )
            }
        }
    }
}
