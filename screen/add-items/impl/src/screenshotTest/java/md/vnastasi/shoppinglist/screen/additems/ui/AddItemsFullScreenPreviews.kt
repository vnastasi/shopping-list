package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.PreviewParameter
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
fun NoSuggestionsFullScreen() {
    ApplicationFullScreenPreview(
        viewState = ViewState(
            suggestions = persistentListOf()
        ),
        searchTermValue = ""
    )
}

@PreviewTest
@ScreenshotPreviews
@Composable
fun SuggestionsAvailableFullScreen(
    @PreviewParameter(ListOfSuggestionsParameterProvider::class, limit = 1) suggestions: ImmutableList<NameSuggestion>
) {
    ApplicationFullScreenPreview(
        viewState = ViewState(
            suggestions = suggestions
        ),
        searchTermValue = "Mil"
    )
}

@Composable
private fun ApplicationFullScreenPreview(
    viewState: ViewState,
    searchTermValue: String
) {
    AppTheme {
        CompositionLocalProvider(LocalPresentationMode provides PresentationMode.FullScreen) {
            AddItemsScreen(
                viewModel = StubAddItemsViewModelSpec(viewState = viewState, searchTermValue = searchTermValue),
                onNavigate = { }
            )
        }
    }
}
