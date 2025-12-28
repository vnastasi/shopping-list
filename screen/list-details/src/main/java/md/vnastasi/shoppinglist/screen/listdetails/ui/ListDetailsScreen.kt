package md.vnastasi.shoppinglist.screen.listdetails.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    ListDetailsScreen(
        viewState = viewState,
        onNavigateUp = if (viewState.shouldShowBackButton) navigator::backToOverview else null,
        onItemClicked = { shoppingItem -> viewModel.onUiEvent(UiEvent.ShoppingItemClicked(shoppingItem)) },
        onItemDeleted = { shoppingItem -> viewModel.onUiEvent(UiEvent.ShoppingItemDeleted(shoppingItem)) },
        onAddNewItems = navigator::toAddItems
    )
}

@Composable
private fun ListDetailsScreen(
    viewState: ViewState,
    onNavigateUp: (() -> Unit)?,
    onItemClicked: (ShoppingItem) -> Unit,
    onItemDeleted: (ShoppingItem) -> Unit,
    onAddNewItems: (Long) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
        topBar = {
            ListDetailsTopAppBar(
                scrollBehavior = scrollBehavior,
                title = viewState.getShoppingListNameOrNull(),
                onNavigateUp = onNavigateUp
            )
        },
        floatingActionButton = {
            val shoppingListId = viewState.getShoppingListIdOrNull()
            if (shoppingListId != null) {
                AddItemsFloatingActionButton(
                    onClick = { onAddNewItems(shoppingListId) }
                )
            }
        }
    ) { contentPaddings ->
        when (viewState) {
            is ViewState.Loading -> {
                AnimatedMessageContent(
                    contentPaddings = contentPaddings,
                    animationResId = R.raw.lottie_anim_loading,
                    messageResId = R.string.list_details_loading
                )
            }

            is ViewState.Empty -> {
                AnimatedMessageContent(
                    contentPaddings = contentPaddings,
                    animationResId = R.raw.lottie_anim_empty_box,
                    messageResId = R.string.list_details_empty
                )
            }

            is ViewState.Ready -> {
                ListDetailsContent(
                    contentPaddings = contentPaddings,
                    listOfShoppingItems = viewState.listOfShoppingItems,
                    onItemClick = onItemClicked,
                    onItemDelete = onItemDeleted
                )
            }
        }
    }
}

@Composable
private fun ListDetailsTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String?,
    onNavigateUp: (() -> Unit)?,
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .testTag(LIST_DETAILS_TOOLBAR),
        windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Top),
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
            if (onNavigateUp != null) {
                IconButton(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Start)),
                    onClick = onNavigateUp
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.list_details_btn_back_acc),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
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
    FloatingActionButton(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Bottom + WindowInsetsSides.End))
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

private fun ViewState.getShoppingListNameOrNull(): String? =
    when (this) {
        is ViewState.Loading -> null
        is ViewState.Empty -> shoppingListName
        is ViewState.Ready -> shoppingListName
    }

private fun ViewState.getShoppingListIdOrNull(): Long? =
    when (this) {
        is ViewState.Loading -> null
        is ViewState.Empty -> shoppingListId
        is ViewState.Ready -> shoppingListId
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
        shouldShowBackButton = true,
        shoppingListId = 1L,
        shoppingListName = "My list",
        listOfShoppingItems = listOfShoppingItems
    )

    AppTheme {
        ListDetailsScreen(
            viewState = viewState,
            onNavigateUp = { },
            onItemClicked = { },
            onItemDeleted = { },
            onAddNewItems = { }
        )
    }
}
