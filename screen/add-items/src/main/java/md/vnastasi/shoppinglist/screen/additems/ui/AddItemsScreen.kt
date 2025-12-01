package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
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
import md.vnastasi.shoppinglist.screen.shared.toast.Toast
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme

@Composable
fun AddItemsScreen(
    viewModel: AddItemsViewModelSpec,
    navigator: AddItemsScreenNavigator
) {
    AddItemsScreen(
        viewState = viewModel.viewState.collectAsStateWithLifecycle(),
        onNavigateUp = navigator::backToListDetails,
        onSearchTermChanged = { value -> viewModel.onUiEvent(UiEvent.SearchTermChanged(value)) },
        onItemAddedToList = { name -> viewModel.onUiEvent(UiEvent.ItemAddedToList(name)) },
        onSuggestionDeleted = { suggestion -> viewModel.onUiEvent(UiEvent.SuggestionDeleted(suggestion)) },
        onToastShown = { viewModel.onUiEvent(UiEvent.ToastShown) }
    )
}

@Composable
private fun AddItemsScreen(
    viewState: State<ViewState>,
    onNavigateUp: () -> Unit,
    onSearchTermChanged: (String) -> Unit,
    onItemAddedToList: (String) -> Unit,
    onSuggestionDeleted: (NameSuggestion) -> Unit,
    onToastShown: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val searchTermTextFieldState = rememberTextFieldState(initialText = "")

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                AddItemsTopAppBar(
                    scrollBehavior = scrollBehavior,
                    searchTermTextFieldState = searchTermTextFieldState,
                    onSearchTermChanged = onSearchTermChanged,
                    onItemAddedToList = onItemAddedToList,
                    onNavigateUp = onNavigateUp
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
                    modifier = Modifier
                        .animateItem()
                        .testTag(SUGGESTIONS_ITEM),
                    suggestion = suggestion,
                    isLastItemInList = index == viewState.value.suggestions.size - 1,
                    isDeletable = suggestion.id != -1L,
                    onClick = {
                        onItemAddedToList(suggestion.name)
                        searchTermTextFieldState.clearText()
                    },
                    onDelete = {
                        onSuggestionDeleted(suggestion)
                    }
                )
            }
        }
    }

    Toast(
        message = viewState.value.toastMessage,
        onToastShown = onToastShown
    )
}

@Composable
private fun AddItemsTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    searchTermTextFieldState: TextFieldState,
    onSearchTermChanged: (String) -> Unit,
    onItemAddedToList: (String) -> Unit,
    onNavigateUp: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Top),
        title = { },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Start))
                    .testTag(BACK_BUTTON),
                onClick = onNavigateUp
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
                    .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Start + WindowInsetsSides.End))
                    .padding(start = 56.dp)
                    .testTag(SEARCH_BAR),
                valueTextFieldState = searchTermTextFieldState,
                onValueChanged = onSearchTermChanged,
                onValueAccepted = onItemAddedToList
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

@ExcludeFromJacocoGeneratedReport
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun AddItemsScreenPreview() {
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
            onNavigateUp = { },
            onSearchTermChanged = { },
            onItemAddedToList = { },
            onSuggestionDeleted = { },
            onToastShown = { }
        )
    }
}
