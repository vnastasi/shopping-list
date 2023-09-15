package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import md.vnastasi.shoppinglist.screen.listdetails.ListDetailsViewModel
import md.vnastasi.shoppinglist.support.state.ScreenState

@Composable
fun ListDetailsScreen(
    navController: NavController,
    viewModel: ListDetailsViewModel
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val toolbarTitle = rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = toolbarTitle.value)
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { contentPaddings ->

        val collectedScreenState = viewModel.screenState.collectAsState()

        when (val screenState = collectedScreenState.value) {
            is ScreenState.Ready -> {
                toolbarTitle.value = screenState.data.name
                if (screenState.data.shoppingItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = contentPaddings.calculateTopPadding(),
                                bottom = contentPaddings.calculateBottomPadding()
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Shopping list is empty",
                            fontSize = 24.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(
                                top = contentPaddings.calculateTopPadding(),
                                bottom = contentPaddings.calculateBottomPadding()
                            )
                            .fillMaxWidth()
                    ) {
                        itemsIndexed(
                            items = screenState.data.shoppingItems,
                            key = { _, shoppingItem -> shoppingItem.id }
                        ) { index, shoppingItem ->
                            ShoppingItemRow(
                                shoppingItem = shoppingItem,
                                isLastItemInList = remember { mutableStateOf(index == screenState.data.shoppingItems.size - 1) },
                                onClick = { }
                            )
                        }
                    }
                }
            }

            else -> Unit
        }
    }
}
