package md.vnastasi.shoppinglist.screen.main

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import md.vnastasi.shoppinglist.domain.model.ShoppingList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableShoppingListsScreen(
    navController: NavHostController,
    viewModel: AvailableShoppingListsViewModel
) {

    val context = LocalContext.current

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
                onClick = {
                    Toast.makeText(context, "FAB clicked", Toast.LENGTH_LONG).show()
                }
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
                onClick = { navController.navigate("shopping-list/$it") },
                onDelete = viewModel::onDelete
            )
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
            ShoppingList(shoppingList = shoppingList, onClickItem = onClick, onDeleteItem = onDelete)
        }
    }
}
