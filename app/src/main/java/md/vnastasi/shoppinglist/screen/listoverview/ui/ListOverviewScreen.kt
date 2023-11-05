package md.vnastasi.shoppinglist.screen.listoverview.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.screen.listoverview.ListOverviewViewModel
import md.vnastasi.shoppinglist.screen.listoverview.NavigationTarget
import md.vnastasi.shoppinglist.screen.listoverview.UiEvent
import md.vnastasi.shoppinglist.screen.listoverview.ViewState
import md.vnastasi.shoppinglist.screen.nav.Routes
import md.vnastasi.shoppinglist.support.ui.bottomsheet.BottomSheetBehaviour

@Composable
fun ListOverviewScreen(
    navController: NavHostController,
    viewModel: ListOverviewViewModel
) {

    ListOverviewScreen(
        viewState = viewModel.screenState.collectAsState(),
        navigationTarget = viewModel.navigationTarget,
        events = Events(
            onAddNewShoppingList = { viewModel.onUiEvent(UiEvent.AddNewShoppingList) },
            onSaveShoppingList = { shoppingListName -> viewModel.onUiEvent(UiEvent.SaveShoppingList(shoppingListName)) },
            onDeleteShoppingList = { shoppingList -> viewModel.onUiEvent(UiEvent.DeleteShoppingList(shoppingList)) },
            onSelectShoppingList = { shoppingList -> viewModel.onUiEvent(UiEvent.SelectShoppingList(shoppingList)) }
        ),
        navigations = Navigations(
            toShoppingListDetails = { shoppingListId -> navController.navigate(Routes.ListDetails(shoppingListId)) }
        )
    )
}

@Stable
private class Events(
    val onAddNewShoppingList: () -> Unit,
    val onSaveShoppingList: (String) -> Unit,
    val onDeleteShoppingList: (ShoppingList) -> Unit,
    val onSelectShoppingList: (ShoppingList) -> Unit
)

@Stable
private class Navigations(
    val toShoppingListDetails: (Long) -> Unit
)

@Composable
private fun ListOverviewScreen(
    viewState: State<ViewState>,
    navigationTarget: Flow<NavigationTarget>,
    events: Events,
    navigations: Navigations
) {

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false,
            initialValue = SheetValue.Hidden
        )
    )
    val bottomSheetScope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            ShoppingListFormBottomSheet(
                behaviour = BottomSheetBehaviour(
                    state = bottomSheetScaffoldState.bottomSheetState,
                    scope = bottomSheetScope
                ),
                onSaveList = events.onSaveShoppingList
            )
        },
        sheetPeekHeight = 0.dp
    ) {

        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            text = "Shopping lists"
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = events.onAddNewShoppingList
                ) {
                    Image(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        ) { contentPaddings ->

            if (viewState.value.shoppingLists.isEmpty()) {
                EmptyListOverviewScreenContent(contentPaddings)
            } else {
                NonEmptyListOverviewScreenContent(
                    contentPaddings = contentPaddings,
                    list = viewState.value.shoppingLists,
                    onClick = events.onSelectShoppingList,
                    onDelete = events.onDeleteShoppingList
                )
            }
        }

        LaunchedEffect(Unit) {
            navigationTarget.collectLatest { navigationTarget ->
                when (navigationTarget) {
                    is NavigationTarget.ShoppingListDetails -> navigations.toShoppingListDetails.invoke(navigationTarget.id)
                    is NavigationTarget.ShoppingListForm -> bottomSheetScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
private fun ListOverviewScreenPreview() {
    val list = persistentListOf(
        ShoppingList(id = 1L, name = "Groceries"),
        ShoppingList(id = 2L, name = "Pharmacy for mom"),
        ShoppingList(id = 3L, name = "Gamma & Praxis"),
        ShoppingList(id = 4L, name = "Birthday party shopping list"),
        ShoppingList(id = 5L, name = "Christmas Eve party"),
        ShoppingList(id = 6L, name = "Thanksgiving family reunion"),
        ShoppingList(id = 7L, name = "Ibiza!"),
        ShoppingList(id = 8L, name = "At the baker's"),
        ShoppingList(id = 9L, name = "Big shopping at the mall"),
        ShoppingList(id = 10L, name = "Trip to Iceland"),
        ShoppingList(id = 11L, name = "Disney"),
        ShoppingList(id = 12L, name = "Trip to Paris"),
    )

    ListOverviewScreen(
        viewState = remember { mutableStateOf(ViewState(list)) },
        navigationTarget = emptyFlow(),
        events = Events(
            onSelectShoppingList = { },
            onDeleteShoppingList = { },
            onSaveShoppingList = { },
            onAddNewShoppingList = { }
        ),
        navigations = Navigations(
            toShoppingListDetails = { }
        )
    )
}
