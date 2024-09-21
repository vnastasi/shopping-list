package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.additems.nav.AddItemsScreenNavigator
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.BACK_BUTTON
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SEARCH_BAR
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SUGGESTIONS_ITEM
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SUGGESTIONS_LIST
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModelSpec
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import md.vnastasi.shoppinglist.support.ui.toast.ToastEffect

@Composable
fun AddItemsScreen(
    viewModel: AddItemsViewModelSpec,
    navigator: AddItemsScreenNavigator
) {
    AddItemsScreen(
        viewState = viewModel.screenState.collectAsStateWithLifecycle(),
        searchTermState = remember { viewModel.searchTermState },
        events = Events(
            onNavigateUp = navigator::backToListDetails,
            onSearchTermChanged = { viewModel.onUiEvent(UiEvent.SearchTermChanged) },
            onItemAddedToList = { viewModel.onUiEvent(UiEvent.ItemAddedToList(it)) },
            onSuggestionDeleted = { suggestion -> viewModel.onUiEvent(UiEvent.SuggestionDeleted(suggestion)) },
            onToastShown = { viewModel.onUiEvent(UiEvent.ToastShown) }
        )
    )
}

@Stable
private class Events(
    val onNavigateUp: () -> Unit,
    val onItemAddedToList: (String) -> Unit,
    val onSearchTermChanged: () -> Unit,
    val onSuggestionDeleted: (NameSuggestion) -> Unit,
    val onToastShown: () -> Unit
)

@Composable
private fun AddItemsScreen(
    viewState: State<ViewState>,
    searchTermState: MutableState<String>,
    events: Events
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = { },
                    navigationIcon = {
                        IconButton(
                            modifier = Modifier
                                .displayCutoutPadding()
                                .testTag(BACK_BUTTON),
                            onClick = events.onNavigateUp
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.add_items_btn_back_acc),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    actions = {
                        SearchBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .displayCutoutPadding()
                                .padding(start = 56.dp)
                                .testTag(SEARCH_BAR),
                            searchTerm = searchTermState,
                            onValueAccepted = { events.onItemAddedToList.invoke(searchTermState.value) }
                        )
                    },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        scrolledContainerColor = TopAppBarDefaults.topAppBarColors().containerColor
                    )
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    ) { contentPaddings ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .testTag(SUGGESTIONS_LIST),
            contentPadding = PaddingValues(
                start = contentPaddings.calculateStartPadding(LocalLayoutDirection.current),
                end = contentPaddings.calculateEndPadding(LocalLayoutDirection.current),
                top = contentPaddings.calculateTopPadding(),
                bottom = contentPaddings.calculateBottomPadding() + AppDimensions.paddingMedium
            )
        ) {
            itemsIndexed(
                items = viewState.value.suggestions,
                key = { _, suggestion -> suggestion.id }
            ) { index, suggestion ->
                SuggestionRow(
                    modifier = Modifier.testTag(SUGGESTIONS_ITEM),
                    suggestion = suggestion,
                    isLastItemInList = index == viewState.value.suggestions.size - 1,
                    isDeletable = suggestion.id != -1L,
                    onClick = { events.onItemAddedToList.invoke(suggestion.name) },
                    onDelete = { events.onSuggestionDeleted.invoke(suggestion) }
                )
            }
        }
    }

    ToastEffect(
        message = viewState.value.toastMessage,
        onToastShown = events.onToastShown
    )

    LaunchedEffect(searchTermState.value) {
        events.onSearchTermChanged.invoke()
    }
}

@ExcludeFromJacocoGeneratedReport
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
@PreviewFontScale
@Composable
fun AddItemsScreenPreview() {
    val viewState = ViewState(
        suggestions = persistentListOf(
            NameSuggestion(id = -1L, "Sample item"),
            NameSuggestion(id = 1L, "Item 1"),
            NameSuggestion(id = 2L, "Item 2"),
            NameSuggestion(id = 3L, "Item 3"),
            NameSuggestion(id = 4L, "Item 4"),
            NameSuggestion(id = 5L, "Item 5"),
            NameSuggestion(id = 6L, "Item 6"),
            NameSuggestion(id = 7L, "Item 7")
        ),
        toastMessage = null
    )

    AppTheme {
        AddItemsScreen(
            viewState = remember { mutableStateOf(viewState) },
            searchTermState = remember { mutableStateOf("") },
            events = Events(
                onNavigateUp = { },
                onItemAddedToList = { },
                onSearchTermChanged = { },
                onSuggestionDeleted = { },
                onToastShown = { }
            )
        )
    }
}
