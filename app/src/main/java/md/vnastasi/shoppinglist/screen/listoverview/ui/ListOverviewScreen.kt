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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.screen.listoverview.ListOverviewViewModel
import md.vnastasi.shoppinglist.screen.listoverview.NavigationTarget
import md.vnastasi.shoppinglist.screen.listoverview.UiEvent
import md.vnastasi.shoppinglist.screen.nav.Routes
import md.vnastasi.shoppinglist.support.state.ScreenState
import md.vnastasi.shoppinglist.support.ui.bottomsheet.BottomSheetBehaviour

@Composable
fun ListOverviewScreen(
    navController: NavHostController,
    viewModel: ListOverviewViewModel
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
                onSaveList = { viewModel.onUiEvent(UiEvent.OnSaveNewShoppingList(it)) }
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
                    onClick = { viewModel.onUiEvent(UiEvent.OnAddNewShoppingListClicked) }
                ) {
                    Image(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        ) { contentPaddings ->

            when (val screenState = viewModel.screenState.collectAsStateWithLifecycle().value) {
                is ScreenState.Loading -> Unit

                is ScreenState.Empty -> EmptyListContent(contentPaddings)

                is ScreenState.Ready -> NonEmptyListContent(
                    contentPaddings = contentPaddings,
                    list = screenState.data,
                    onClick = { viewModel.onUiEvent(UiEvent.OnShoppingListItemClicked(it)) },
                    onDelete = { viewModel.onUiEvent(UiEvent.OnShoppingListItemDeleted(it)) }
                )

                is ScreenState.Failure -> Unit
            }
        }

        LaunchedEffect(key1 = Unit) {
            viewModel.navigationTarget.collectLatest { navigationTarget ->
                when (navigationTarget) {
                    is NavigationTarget.ShoppingListDetails -> navController.navigate(Routes.ListDetails(navigationTarget.id))
                    is NavigationTarget.ShoppingListForm -> bottomSheetScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }
                }
            }
        }
    }
}
