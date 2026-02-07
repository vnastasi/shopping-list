package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.overview.nav.OverviewScreenNavigator
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.NEW_SHOPPING_LIST_FAB
import md.vnastasi.shoppinglist.screen.overview.vm.OverviewViewModelSpec
import md.vnastasi.shoppinglist.screen.shared.content.AnimatedMessageContent
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme

@Composable
fun OverviewScreen(
    viewModel: OverviewViewModelSpec,
    navigator: OverviewScreenNavigator
) {
    OverviewScreen(
        viewState = viewModel.viewState.collectAsStateWithLifecycle(),
        onAddNewShoppingList = { navigator.openManageListSheet(null) },
        onShoppingListEdited = { shoppingList -> navigator.openManageListSheet(shoppingList.id) },
        onShoppingListDeleted = { shoppingList -> viewModel.onUiEvent(UiEvent.ShoppingListDeleted(shoppingList)) },
        onShoppingListSelected = { shoppingList -> navigator.toListDetails(shoppingList.id) },
        onShoppingListsReordered = { reorderedList -> viewModel.onUiEvent(UiEvent.ShoppingListsReordered(reorderedList)) }
    )
}

@Composable
private fun OverviewScreen(
    viewState: State<ViewState>,
    onAddNewShoppingList: () -> Unit,
    onShoppingListEdited: (ShoppingListDetails) -> Unit,
    onShoppingListDeleted: (ShoppingListDetails) -> Unit,
    onShoppingListSelected: (ShoppingListDetails) -> Unit,
    onShoppingListsReordered: (List<ShoppingListDetails>) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val showFloatingActionButton by remember { derivedStateOf { viewState.value != ViewState.Loading } }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
        topBar = {
            OverviewTopAppBar(
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            if (showFloatingActionButton) {
                ManageListFloatingActionButton(
                    onClick = onAddNewShoppingList
                )
            }
        }
    ) { contentPaddings ->
        when (val localViewState = viewState.value) {
            is ViewState.Loading -> {
                AnimatedMessageContent(
                    contentPaddings = contentPaddings,
                    animationResId = R.raw.lottie_anim_loading,
                    messageResId = R.string.overview_loading
                )
            }

            is ViewState.Empty -> {
                AnimatedMessageContent(
                    contentPaddings = contentPaddings,
                    animationResId = R.raw.lottie_anim_shopping_cart,
                    messageResId = R.string.overview_empty_list
                )
            }

            is ViewState.Ready -> {
                ReadyOverviewContent(
                    contentPaddings = contentPaddings,
                    shoppingLists = localViewState.shoppingLists,
                    onShoppingListEdited = onShoppingListEdited,
                    onShoppingListDeleted = onShoppingListDeleted,
                    onShoppingListSelected = onShoppingListSelected,
                    onShoppingListsReordered = onShoppingListsReordered
                )
            }
        }
    }
}

@Composable
private fun OverviewTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Top),
        title = {
            Text(
                text = stringResource(R.string.overview_toolbar_title)
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun ManageListFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Bottom + WindowInsetsSides.End))
            .testTag(NEW_SHOPPING_LIST_FAB),
        shape = RoundedCornerShape(size = AppDimensions.paddingMedium),
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.overview_btn_add_list_acc)
        )
    }
}

@Composable
private fun ReadyOverviewContent(
    contentPaddings: PaddingValues,
    shoppingLists: ImmutableList<ShoppingListDetails>,
    onShoppingListEdited: (ShoppingListDetails) -> Unit,
    onShoppingListDeleted: (ShoppingListDetails) -> Unit,
    onShoppingListSelected: (ShoppingListDetails) -> Unit,
    onShoppingListsReordered: (List<ShoppingListDetails>) -> Unit
) {
    OverviewContent(
        contentPaddings = contentPaddings,
        list = shoppingLists,
        onEdit = onShoppingListEdited,
        onClick = onShoppingListSelected,
        onDelete = onShoppingListDeleted,
        onReorder = onShoppingListsReordered
    )
}

@ExcludeFromJacocoGeneratedReport
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun ListOverviewScreenPreview() {
    val list = persistentListOf(
        ShoppingListDetails(id = 1L, name = "Groceries", position = 1L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 2L, name = "Pharmacy for mom", position = 2L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 3L, name = "Gamma & Praxis", position = 3L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 4L, name = "Birthday party shopping list", position = 4L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 5L, name = "Christmas Eve party", position = 5L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 6L, name = "Thanksgiving family reunion", position = 6L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 7L, name = "Ibiza!", position = 7L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 8L, name = "At the baker's", position = 8L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 9L, name = "Big shopping at the mall", position = 9L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 10L, name = "Trip to Iceland", position = 10L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 11L, name = "Disney", position = 11L, totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 12L, name = "Trip to Paris", position = 12L, totalItems = 0L, checkedItems = 0L),
    )

    AppTheme {
        OverviewScreen(
            viewState = remember { mutableStateOf<ViewState>(ViewState.Ready(list)) },
            onShoppingListSelected = { },
            onShoppingListEdited = { },
            onShoppingListDeleted = { },
            onAddNewShoppingList = { },
            onShoppingListsReordered = { }
        )
    }
}
