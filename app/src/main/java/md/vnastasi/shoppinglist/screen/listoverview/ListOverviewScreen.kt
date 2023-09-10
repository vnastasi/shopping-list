package md.vnastasi.shoppinglist.screen.listoverview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingList

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
            ShoppingListFormBottomSheet(bottomSheetScaffoldState.bottomSheetState, bottomSheetScope, viewModel)
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
                    onClick = viewModel::onAddShoppingListClicked
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

                is ScreenState.NoEntries -> EmptyState(contentPaddings)

                is ScreenState.AvailableEntries -> AvailableShoppingListsState(
                    contentPaddings = contentPaddings,
                    list = screenState.list,
                    onClick = viewModel::onListItemClick,
                    onDelete = viewModel::onListItemDelete
                )
            }
        }

        LaunchedEffect(key1 = Unit) {
            viewModel.navigationTarget.collectLatest { navigationTarget ->
                when (navigationTarget) {
                    is NavigationTarget.ShoppingListDetails -> navController.navigate("shopping-list/${navigationTarget.id}")
                    is NavigationTarget.ShoppingListForm -> bottomSheetScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }
                }
            }
        }
    }
}

@Composable
fun EmptyState(
    contentPaddings: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = contentPaddings.calculateTopPadding(), bottom = contentPaddings.calculateBottomPadding()),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No shopping lists available"
        )
    }
}

@Composable
fun AvailableShoppingListsState(
    contentPaddings: PaddingValues,
    list: List<ShoppingList>,
    onDelete: (ShoppingList) -> Unit = { },
    onClick: (ShoppingList) -> Unit = { }
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = contentPaddings.calculateTopPadding(), bottom = contentPaddings.calculateBottomPadding())
            .fillMaxWidth()
    ) {
        items(items = list, key = { it.id }) { shoppingList ->
            ShoppingListCard(list = shoppingList, onClickItem = onClick, onDeleteItem = onDelete)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListFormBottomSheet(
    bottomSheetState: SheetState,
    bottomSheetScope: CoroutineScope,
    viewModel: ListOverviewViewModel
) {

    val textFieldValue = rememberSaveable { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {

        OutlinedTextField(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            value = textFieldValue.value,
            label = {
                Text(text = "Shopping list name")
            },
            maxLines = 1,
            singleLine = true,
            onValueChange = { newValue ->
                textFieldValue.value = newValue
            }
        )

        IconButton(
            modifier = Modifier.padding(start = 8.dp),
            onClick = {
                viewModel.onSaveNewShoppingList(textFieldValue.value)
                bottomSheetScope.launch { bottomSheetState.hide() }
            }
        ) {

        }

        Button(
            modifier = Modifier.padding(start = 8.dp),
            shape = RoundedCornerShape(50),
            onClick = {
                viewModel.onSaveNewShoppingList(textFieldValue.value)
                bottomSheetScope.launch { bottomSheetState.hide() }
            }
        ) {
            Image(
                imageVector = Icons.Default.Create,
                contentDescription = null
            )
        }
    }

    LaunchedEffect(bottomSheetState.currentValue) {
        if (bottomSheetState.currentValue == SheetValue.Hidden) {
            textFieldValue.value = ""
        }
    }
}
