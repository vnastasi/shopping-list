package md.vnastasi.shoppinglist.screen.additems.ui

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.screen.additems.AddItemsViewModel
import md.vnastasi.shoppinglist.screen.additems.UiEvent
import md.vnastasi.shoppinglist.screen.additems.ViewState

@Composable
fun AddItemsScreen(
    navController: NavController,
    viewModel: AddItemsViewModel
) {
    AddItemsScreen(
        viewState = viewModel.screenState.collectAsStateWithLifecycle(),
        events = Events(
            onNavigateUp = { navController.navigateUp() },
            onSearchTermChanged = { searchTerm -> viewModel.onUiEvent(UiEvent.SearchTermChanged(searchTerm)) },
            onItemAddedToList = { suggestedName -> viewModel.onUiEvent(UiEvent.ItemAddedToList(suggestedName)) },
            onSuggestionDeleted = { suggestion -> viewModel.onUiEvent(UiEvent.SuggestionDeleted(suggestion)) },
            onToastShown = { viewModel.onUiEvent(UiEvent.ToastShown) }
        )
    )
}

@Stable
private class Events(
    val onNavigateUp: () -> Unit,
    val onItemAddedToList: (String) -> Unit,
    val onSearchTermChanged: (String) -> Unit,
    val onSuggestionDeleted: (NameSuggestion) -> Unit,
    val onToastShown: () -> Unit
)

@Composable
private fun AddItemsScreen(
    viewState: State<ViewState>,
    events: Events
) {

    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val textFieldValue = rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(text = "")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            events.onNavigateUp.invoke()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 56.dp),
                        searchTerm = textFieldValue
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { contentPaddings ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = contentPaddings.calculateTopPadding())
        ) {
            itemsIndexed(
                items = viewState.value.suggestions,
                key = { _, suggestion -> suggestion.id }
            ) { index, suggestion ->
                SuggestionRow(
                    suggestion = suggestion,
                    isLastItemInList = index == viewState.value.suggestions.size - 1,
                    isDeletable = suggestion.id != -1L,
                    onClick = { events.onItemAddedToList.invoke(suggestion.name) },
                    onDelete = { events.onSuggestionDeleted.invoke(suggestion) }
                )
            }
        }
    }

    LaunchedEffect(key1 = textFieldValue.value) {
        events.onSearchTermChanged.invoke(textFieldValue.value)
    }

    LaunchedEffect(key1 = viewState.value.toastMessage) {
        if (viewState.value.toastMessage != null) {
            Toast.makeText(context, viewState.value.toastMessage, Toast.LENGTH_SHORT).show()
            events.onToastShown.invoke()
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
fun AddItemsScreenPreview() {
    val viewState = ViewState(
        searchTerm = "Search term",
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

    AddItemsScreen(
        viewState = remember { mutableStateOf(viewState) },
        events = Events(
            onNavigateUp = { },
            onItemAddedToList = { },
            onSearchTermChanged = { },
            onSuggestionDeleted = { },
            onToastShown = { }
        )
    )
}