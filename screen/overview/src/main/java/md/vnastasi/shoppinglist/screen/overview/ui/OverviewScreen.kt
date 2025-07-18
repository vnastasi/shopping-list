package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.overview.nav.OverviewScreenNavigator
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.NEW_SHOPPING_LIST_FAB
import md.vnastasi.shoppinglist.screen.overview.vm.OverviewViewModelSpec
import md.vnastasi.shoppinglist.screen.shared.bottomsheet.BottomSheetBehaviour
import md.vnastasi.shoppinglist.screen.shared.content.AnimatedMessageContent
import md.vnastasi.shoppinglist.screen.shared.toast.Toast
import md.vnastasi.shoppinglist.screen.shared.toast.ToastMessage
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
        onAddNewShoppingList = { viewModel.onUiEvent(UiEvent.AddNewShoppingList) },
        onShoppingListSaved = { shoppingListName -> viewModel.onUiEvent(UiEvent.ShoppingListSaved(shoppingListName)) },
        onShoppingListDeleted = { shoppingList -> viewModel.onUiEvent(UiEvent.ShoppingListDeleted(shoppingList)) },
        onShoppingListSelected = { shoppingList -> viewModel.onUiEvent(UiEvent.ShoppingListSelected(shoppingList)) },
        onToastShown = { viewModel.onUiEvent(UiEvent.ToastShown) },
        onNavigateToListDetails = navigator::toListDetails,
        onNavigationPerformed = { viewModel.onUiEvent(UiEvent.NavigationPerformed) }
    )
}

@Composable
private fun OverviewScreen(
    viewState: State<ViewState>,
    onAddNewShoppingList: () -> Unit,
    onShoppingListSaved: (String) -> Unit,
    onShoppingListDeleted: (ShoppingListDetails) -> Unit,
    onShoppingListSelected: (ShoppingListDetails) -> Unit,
    onToastShown: () -> Unit,
    onNavigateToListDetails: (Long) -> Unit,
    onNavigationPerformed: () -> Unit
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            skipHiddenState = false,
            initialValue = SheetValue.Hidden
        )
    )
    val bottomSheetBehaviour = BottomSheetBehaviour(
        state = bottomSheetScaffoldState.bottomSheetState,
        scope = rememberCoroutineScope()
    )

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            ShoppingListFormBottomSheet(
                modifier = Modifier.imePadding(),
                behaviour = bottomSheetBehaviour,
                onShoppingListSaved = onShoppingListSaved
            )
        },
        sheetPeekHeight = AppDimensions.zero
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
                    NewShoppingListFloatingActionButton(
                        onClick = onAddNewShoppingList
                    )
                }
            }
        ) { contentPaddings ->
            val localViewState = viewState.value
            when (localViewState) {
                is ViewState.Loading -> {
                    AnimatedMessageContent(
                        contentPaddings = contentPaddings,
                        animationResId = R.raw.lottie_anim_loading,
                        messageResId = R.string.overview_loading
                    )
                }

                is ViewState.Empty -> {
                    EmptyOverviewContent(
                        contentPaddings = contentPaddings,
                        bottomSheetBehaviour = bottomSheetBehaviour,
                        toastMessage = localViewState.toastMessage,
                        navigationTarget = localViewState.navigationTarget,
                        onToastShown = onToastShown,
                        onNavigateToListDetails = onNavigateToListDetails,
                        onNavigationPerformed = onNavigationPerformed
                    )
                }

                is ViewState.Ready -> {
                    ReadyOverviewContent(
                        contentPaddings = contentPaddings,
                        bottomSheetBehaviour = bottomSheetBehaviour,
                        shoppingLists = localViewState.shoppingLists,
                        toastMessage = localViewState.toastMessage,
                        navigationTarget = localViewState.navigationTarget,
                        onShoppingListDeleted = onShoppingListDeleted,
                        onShoppingListSelected = onShoppingListSelected,
                        onToastShown = onToastShown,
                        onNavigateToListDetails = onNavigateToListDetails,
                        onNavigationPerformed = onNavigationPerformed
                    )
                }
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
private fun NewShoppingListFloatingActionButton(
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
private fun EmptyOverviewContent(
    contentPaddings: PaddingValues,
    bottomSheetBehaviour: BottomSheetBehaviour,
    toastMessage: ToastMessage?,
    navigationTarget: NavigationTarget?,
    onToastShown: () -> Unit,
    onNavigateToListDetails: (Long) -> Unit,
    onNavigationPerformed: () -> Unit
) {
    Toast(
        message = toastMessage,
        onToastShown = onToastShown
    )

    NavigationHandler(
        bottomSheetBehaviour = bottomSheetBehaviour,
        navigationTarget = navigationTarget,
        onNavigateToListDetails = onNavigateToListDetails,
        onNavigationPerformed = onNavigationPerformed
    )

    AnimatedMessageContent(
        contentPaddings = contentPaddings,
        animationResId = R.raw.lottie_anim_shopping_cart,
        messageResId = R.string.overview_empty_list
    )
}

@Composable
private fun ReadyOverviewContent(
    contentPaddings: PaddingValues,
    bottomSheetBehaviour: BottomSheetBehaviour,
    shoppingLists: ImmutableList<ShoppingListDetails>,
    toastMessage: ToastMessage?,
    navigationTarget: NavigationTarget?,
    onShoppingListDeleted: (ShoppingListDetails) -> Unit,
    onShoppingListSelected: (ShoppingListDetails) -> Unit,
    onToastShown: () -> Unit,
    onNavigateToListDetails: (Long) -> Unit,
    onNavigationPerformed: () -> Unit
) {
    Toast(
        message = toastMessage,
        onToastShown = onToastShown
    )

    NavigationHandler(
        bottomSheetBehaviour = bottomSheetBehaviour,
        navigationTarget = navigationTarget,
        onNavigateToListDetails = onNavigateToListDetails,
        onNavigationPerformed = onNavigationPerformed
    )

    OverviewContent(
        contentPaddings = contentPaddings,
        list = shoppingLists,
        onClick = onShoppingListSelected,
        onDelete = onShoppingListDeleted
    )
}

@Composable
private fun NavigationHandler(
    bottomSheetBehaviour: BottomSheetBehaviour,
    navigationTarget: NavigationTarget?,
    onNavigateToListDetails: (Long) -> Unit,
    onNavigationPerformed: () -> Unit
) {
    LaunchedEffect(navigationTarget) {
        when (navigationTarget) {
            is NavigationTarget.ShoppingListDetails -> {
                onNavigateToListDetails(navigationTarget.id)
                onNavigationPerformed()
            }

            is NavigationTarget.ShoppingListForm -> {
                bottomSheetBehaviour.scope.launch { bottomSheetBehaviour.state.expand() }
                onNavigationPerformed()
            }

            null -> Unit
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun ListOverviewScreenPreview() {
    val list = persistentListOf(
        ShoppingListDetails(id = 1L, name = "Groceries", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 2L, name = "Pharmacy for mom", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 3L, name = "Gamma & Praxis", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 4L, name = "Birthday party shopping list", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 5L, name = "Christmas Eve party", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 6L, name = "Thanksgiving family reunion", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 7L, name = "Ibiza!", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 8L, name = "At the baker's", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 9L, name = "Big shopping at the mall", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 10L, name = "Trip to Iceland", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 11L, name = "Disney", totalItems = 0L, checkedItems = 0L),
        ShoppingListDetails(id = 12L, name = "Trip to Paris", totalItems = 0L, checkedItems = 0L),
    )

    AppTheme {
        OverviewScreen(
            viewState = remember { mutableStateOf<ViewState>(ViewState.Ready(list)) },
            onShoppingListSelected = { },
            onShoppingListDeleted = { },
            onShoppingListSaved = { },
            onAddNewShoppingList = { },
            onNavigationPerformed = { },
            onToastShown = { },
            onNavigateToListDetails = { }
        )
    }
}
