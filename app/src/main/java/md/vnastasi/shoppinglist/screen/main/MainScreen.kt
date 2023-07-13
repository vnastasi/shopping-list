package md.vnastasi.shoppinglist.screen.main

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import md.vnastasi.shoppinglist.domain.model.ShoppingList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {

    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

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
        LazyColumn(
            modifier = Modifier
                .padding(top = contentPaddings.calculateTopPadding(), bottom = contentPaddings.calculateBottomPadding())
                .fillMaxWidth()
        ) {
            items(
                items = tempShoppingListItems,
                key = { it.id }
            ) { shoppingList ->
                ShoppingListItem(
                    shoppingList = shoppingList,
                    onClickItem = {
                        navController.navigate("shopping-list/${shoppingList.id}")
                    },
                    onDeleteItem = {
                        Toast.makeText(context, "Shopping list ${shoppingList.id} deleted", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

val tempShoppingListItems = listOf(
    ShoppingList(1, "Shopping List 1"),
    ShoppingList(2, "Shopping List 2"),
    ShoppingList(3, "Shopping List 3"),
    ShoppingList(4, "Shopping List 4"),
    ShoppingList(5, "Shopping List 5"),
    ShoppingList(6, "Shopping List 6"),
    ShoppingList(7, "Shopping List 7"),
    ShoppingList(8, "Shopping List 8"),
    ShoppingList(9, "Shopping List 9"),
    ShoppingList(10, "Shopping List 10"),
    ShoppingList(11, "Shopping List 11"),
    ShoppingList(12, "Shopping List 12"),
    ShoppingList(13, "Shopping List 13"),
    ShoppingList(14, "Shopping List 14"),
    ShoppingList(15, "Shopping List 15"),
    ShoppingList(16, "Shopping List 16"),
    ShoppingList(17, "Shopping List 17"),
    ShoppingList(18, "Shopping List 18"),
    ShoppingList(19, "Shopping List 19"),
    ShoppingList(20, "Shopping List 20"),
    ShoppingList(21, "Shopping List 21"),
    ShoppingList(22, "Shopping List 22"),
    ShoppingList(23, "Shopping List 23"),
)
