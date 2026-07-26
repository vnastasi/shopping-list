package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.window.Dialog
import com.android.tools.screenshot.PreviewTest
import kotlinx.collections.immutable.ImmutableList
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
internal fun NoSuggestionsDialog() {
    ApplicationDialogPreview(
        viewState = ViewState(
            suggestions = persistentListOf()
        ),
        searchTermValue = ""
    )
}

@PreviewTest
@ScreenshotPreviews
@Composable
internal fun SuggestionsAvailableDialog(
    @PreviewParameter(ListOfSuggestionsParameterProvider::class, limit = 1) suggestions: ImmutableList<NameSuggestion>
) {
    ApplicationDialogPreview(
        viewState = ViewState(
            suggestions = suggestions
        ),
        searchTermValue = "Mil"
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
                    onNavigate = { }
                )
            }
        }
    }
}
