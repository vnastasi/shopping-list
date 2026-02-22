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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.BACK_BUTTON
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SEARCH_BAR
import md.vnastasi.shoppinglist.screen.shared.toast.Toast
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme

@Composable
internal fun AddItemsFullScreen(
    searchTermTextFieldState: TextFieldState,
    viewState: ViewState,
    dispatchEvent: (UiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
                    onItemAddedToList = { dispatchEvent(UiEvent.OnItemAddedToList(it)) },
                    onNavigateUp = { dispatchEvent(UiEvent.OnBackClicked) }
                )
            }
        }
    ) { contentPaddings ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .testTag(TestTags.SUGGESTIONS_LIST),
            contentPadding = PaddingValues(
                start = contentPaddings.calculateStartPadding(LocalLayoutDirection.current),
                end = contentPaddings.calculateEndPadding(LocalLayoutDirection.current),
                top = contentPaddings.calculateTopPadding(),
                bottom = contentPaddings.calculateBottomPadding() + AppDimensions.paddingMedium
            )
        ) {
            itemsIndexed(
                items = viewState.suggestions,
                key = { _, suggestion -> suggestion.id }
            ) { index, suggestion ->
                SuggestionRow(
                    modifier = Modifier
                        .animateItem()
                        .testTag(TestTags.SUGGESTIONS_ITEM),
                    suggestion = suggestion,
                    isLastItemInList = index == viewState.suggestions.size - 1,
                    isDeletable = suggestion.id != -1L,
                    onClick = { dispatchEvent(UiEvent.OnItemAddedToList(suggestion.name)) },
                    onDelete = { dispatchEvent(UiEvent.OnSuggestionDeleted(suggestion)) }
                )
            }
        }
    }

    Toast(
        message = viewState.toastMessage,
        onToastShown = { dispatchEvent(UiEvent.OnToastShown) }
    )
}

@Composable
private fun AddItemsTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    searchTermTextFieldState: TextFieldState,
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

@Composable
internal fun SearchBar(
    modifier: Modifier = Modifier,
    valueTextFieldState: TextFieldState,
    onValueAccepted: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        modifier = modifier.focusRequester(focusRequester),
        state = valueTextFieldState,
        placeholder = {
            Text(text = stringResource(R.string.add_items_search_title))
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        trailingIcon = {
            IconButton(
                onClick = { valueTextFieldState.clearText() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null
                )
            }
        },
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        onKeyboardAction = KeyboardActionHandler {
            onValueAccepted.invoke(valueTextFieldState.text.toString())
            valueTextFieldState.clearText()
        }
    )
}

@ExcludeFromJacocoGeneratedReport
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun AddItemsFullScreenPreview() {
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
        toastMessage = null,
        navigationTarget = null
    )

    AppTheme {
        AddItemsFullScreen(
            searchTermTextFieldState = rememberTextFieldState(),
            viewState = viewState,
            dispatchEvent = { }
        )
    }
}
