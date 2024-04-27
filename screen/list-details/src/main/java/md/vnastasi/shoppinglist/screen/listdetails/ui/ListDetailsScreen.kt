package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.listdetails.nav.ListDetailsScreenNavigator
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModelSpec
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme

@Composable
fun ListDetailsScreen(
    viewModel: ListDetailsViewModelSpec,
    navigator: ListDetailsScreenNavigator
) {
    ListDetailsScreen(
        viewState = viewModel.screenState.collectAsState(),
        events = Events(
            onNavigateUp = navigator::backToOverview,
            onItemClicked = { shoppingItem -> viewModel.onUiEvent(UiEvent.ShoppingItemClicked(shoppingItem)) },
            onAddNewItems = { shoppingListId -> navigator.toAddItems(shoppingListId) }
        )
    )
}

@Stable
private class Events(
    val onNavigateUp: () -> Unit,
    val onItemClicked: (ShoppingItem) -> Unit,
    val onAddNewItems: (Long) -> Unit
)

@Composable
private fun ListDetailsScreen(
    viewState: State<ViewState>,
    events: Events
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(text = viewState.value.shoppingListName)
                },
                navigationIcon = {
                    IconButton(
                        onClick = events.onNavigateUp
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.list_details_btn_back_acc),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = RoundedCornerShape(size = AppDimensions.paddingMedium),
                onClick = { events.onAddNewItems.invoke(viewState.value.shoppingListId) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.list_details_btn_add_acc)
                )
            }
        }
    ) { contentPaddings ->
        if (viewState.value.listOfShoppingItems.isEmpty()) {
            EmptyListDetailsScreenContent(
                contentPaddings = contentPaddings
            )
        } else {
            NonEmptyListDetailsScreenContent(
                contentPaddings = contentPaddings,
                listOfShoppingItems = viewState.value.listOfShoppingItems,
                onClick = events.onItemClicked
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFBFE
)
@Composable
private fun ListDetailsScreenPreview() {
    val shoppingList = ShoppingList(id = 1L, "My list")
    val listOfShoppingItems = persistentListOf(
        ShoppingItem(id = 1L, name = "Apples", isChecked = true, list = shoppingList),
        ShoppingItem(id = 2L, name = "Bread", isChecked = false, list = shoppingList),
        ShoppingItem(id = 3L, name = "Minced meat", isChecked = true, list = shoppingList),
        ShoppingItem(id = 4L, name = "Deodorant", isChecked = false, list = shoppingList),
    )
    val viewState = ViewState(
        shoppingListId = 1L,
        shoppingListName = "My list",
        listOfShoppingItems = listOfShoppingItems
    )

    AppTheme {
        ListDetailsScreen(
            viewState = remember { mutableStateOf(viewState) },
            events = Events(
                onNavigateUp = { },
                onItemClicked = { },
                onAddNewItems = { }
            )
        )
    }
}
