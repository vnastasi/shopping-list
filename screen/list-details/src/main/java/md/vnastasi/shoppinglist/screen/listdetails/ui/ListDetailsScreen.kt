package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.max
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.screen.listdetails.nav.ListDetailsScreenNavigator
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.ADD_SHOPPING_LIST_ITEMS_FAB
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.LIST_DETAILS_TOOLBAR
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModelSpec
import md.vnastasi.shoppinglist.screen.shared.content.AnimatedMessageContent
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme

@Composable
fun ListDetailsScreen(
    viewModel: ListDetailsViewModelSpec,
    navigator: ListDetailsScreenNavigator
) {
    ListDetailsScreen(
        viewState = viewModel.viewState.collectAsStateWithLifecycle(),
        onNavigateUp = navigator::backToOverview,
        onItemClicked = { shoppingItem -> viewModel.onUiEvent(UiEvent.ShoppingItemClicked(shoppingItem)) },
        onAddNewItems = { shoppingListId -> navigator.toAddItems(shoppingListId) }
    )
}

@Composable
private fun ListDetailsScreen(
    viewState: State<ViewState>,
    onNavigateUp: () -> Unit,
    onItemClicked: (ShoppingItem) -> Unit,
    onAddNewItems: (Long) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ListDetailsTopAppBar(
                scrollBehavior = scrollBehavior,
                title = (viewState.value as? ViewState.Ready)?.shoppingListName,
                onNavigateUp = onNavigateUp
            )
        },
        floatingActionButton = {
            val viewStateValue = viewState.value
            if (viewStateValue is ViewState.Ready) {
                AddItemsFloatingActionButton(
                    onClick = { onAddNewItems(viewStateValue.shoppingListId) }
                )
            }
        }
    ) { contentPaddings ->
        Crossfade(
            modifier = Modifier.fillMaxSize(),
            targetState = viewState.value,
            label = "List details cross-fade"
        ) { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    AnimatedMessageContent(
                        contentPaddings = contentPaddings,
                        animationResId = R.raw.lottie_anim_loading,
                        messageResId = R.string.list_details_loading
                    )
                }

                is ViewState.Ready -> {
                    ReadyListDetailsContent(
                        contentPaddings = contentPaddings,
                        listState = listState,
                        listOfShoppingItems = viewState.listOfShoppingItems,
                        onItemClick = onItemClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun ListDetailsTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String?,
    onNavigateUp: () -> Unit,
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .testTag(LIST_DETAILS_TOOLBAR),
        title = {
            AnimatedVisibility(
                visible = !title.isNullOrBlank(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(text = title.orEmpty())
            }
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.displayCutoutPadding(),
                onClick = onNavigateUp
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
}

@Composable
private fun AddItemsFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val navBarEndPadding = WindowInsets.navigationBars.asPaddingValues().calculateEndPadding(LocalLayoutDirection.current)
    val displayCutoutEndPadding = WindowInsets.displayCutout.asPaddingValues().calculateEndPadding(LocalLayoutDirection.current)
    val fabEndPadding = max(navBarEndPadding, displayCutoutEndPadding)

    FloatingActionButton(
        modifier = modifier
            .padding(end = fabEndPadding)
            .testTag(ADD_SHOPPING_LIST_ITEMS_FAB),
        shape = RoundedCornerShape(size = AppDimensions.paddingMedium),
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.list_details_btn_add_acc)
        )
    }
}

@Composable
private fun ReadyListDetailsContent(
    contentPaddings: PaddingValues,
    listState: LazyListState,
    listOfShoppingItems: ImmutableList<ShoppingItem>,
    onItemClick: (ShoppingItem) -> Unit
) {
    if (listOfShoppingItems.isEmpty()) {
        AnimatedMessageContent(
            contentPaddings = contentPaddings,
            animationResId = R.raw.lottie_anim_empty_box,
            messageResId = R.string.list_details_empty
        )
    } else {
        ListDetailsContent(
            contentPaddings = contentPaddings,
            listState = listState,
            listOfShoppingItems = listOfShoppingItems,
            onItemClick = onItemClick
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun ListDetailsScreenPreview() {
    val shoppingList = ShoppingList(id = 1L, "My list")
    val listOfShoppingItems = persistentListOf(
        ShoppingItem(id = 1L, name = "Apples", isChecked = true, list = shoppingList),
        ShoppingItem(id = 2L, name = "Bread", isChecked = false, list = shoppingList),
        ShoppingItem(id = 3L, name = "Minced meat", isChecked = true, list = shoppingList),
        ShoppingItem(id = 4L, name = "Deodorant", isChecked = false, list = shoppingList),
    )
    val viewState = ViewState.Ready(
        shoppingListId = 1L,
        shoppingListName = "My list",
        listOfShoppingItems = listOfShoppingItems
    )

    AppTheme {
        ListDetailsScreen(
            viewState = remember { mutableStateOf(viewState) },
            onNavigateUp = { },
            onItemClicked = { },
            onAddNewItems = { }
        )
    }
}
