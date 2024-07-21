package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.overview.nav.ListOverviewScreenNavigator
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.NEW_SHOPPING_LIST_FAB
import md.vnastasi.shoppinglist.screen.overview.vm.ListOverviewViewModelSpec
import md.vnastasi.shoppinglist.support.theme.AppDimensions
import md.vnastasi.shoppinglist.support.theme.AppTheme
import md.vnastasi.shoppinglist.support.ui.bottomsheet.BottomSheetBehaviour
import md.vnastasi.shoppinglist.support.ui.toast.ToastEffect

@Composable
fun ListOverviewScreen(
    viewModel: ListOverviewViewModelSpec,
    navigator: ListOverviewScreenNavigator
) {

    ListOverviewScreen(
        viewState = viewModel.screenState.collectAsStateWithLifecycle(),
        events = Events(
            onAddNewShoppingList = { viewModel.onUiEvent(UiEvent.AddNewShoppingList) },
            onShoppingListSaved = { shoppingListName -> viewModel.onUiEvent(UiEvent.ShoppingListSaved(shoppingListName)) },
            onShoppingListDeleted = { shoppingList -> viewModel.onUiEvent(UiEvent.ShoppingListDeleted(shoppingList)) },
            onShoppingListSelected = { shoppingList -> viewModel.onUiEvent(UiEvent.ShoppingListSelected(shoppingList)) },
            onNavigationPerformed = { viewModel.onUiEvent(UiEvent.NavigationPerformed) },
            onToastShown = { viewModel.onUiEvent(UiEvent.ToastShown) },
            onNavigateToListDetails = navigator::toListDetails
        )
    )
}

@Stable
private data class Events(
    val onAddNewShoppingList: () -> Unit,
    val onShoppingListSaved: (String) -> Unit,
    val onShoppingListDeleted: (ShoppingListDetails) -> Unit,
    val onShoppingListSelected: (ShoppingListDetails) -> Unit,
    val onNavigationPerformed: () -> Unit,
    val onToastShown: () -> Unit,
    val onNavigateToListDetails: (Long) -> Unit
)

@Composable
private fun ListOverviewScreen(
    viewState: State<ViewState>,
    events: Events
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
                modifier = Modifier.imePadding(),
                behaviour = BottomSheetBehaviour(
                    state = bottomSheetScaffoldState.bottomSheetState,
                    scope = bottomSheetScope
                ),
                onShoppingListSaved = events.onShoppingListSaved
            )
        },
        sheetPeekHeight = AppDimensions.zero
    ) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            text = stringResource(R.string.overview_toolbar_title)
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                val navBarEndPadding = WindowInsets.navigationBars.asPaddingValues().calculateEndPadding(LocalLayoutDirection.current)
                val displayCutoutEndPadding = WindowInsets.displayCutout.asPaddingValues().calculateEndPadding(LocalLayoutDirection.current)
                val fabEndPadding = max(navBarEndPadding, displayCutoutEndPadding)

                FloatingActionButton(
                    modifier = Modifier
                        .padding(end = fabEndPadding)
                        .testTag(NEW_SHOPPING_LIST_FAB),
                    shape = RoundedCornerShape(size = AppDimensions.paddingMedium),
                    onClick = events.onAddNewShoppingList
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.overview_btn_add_list_acc)
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
                    onClick = events.onShoppingListSelected,
                    onDelete = events.onShoppingListDeleted
                )
            }
        }
    }

    ToastEffect(
        message = viewState.value.toastMessage,
        onToastShown = events.onToastShown
    )

    LaunchedEffect(key1 = viewState.value.navigationTarget) {
        when (val navigationTarget = viewState.value.navigationTarget) {
            is NavigationTarget.ShoppingListDetails -> {
                events.onNavigateToListDetails.invoke(navigationTarget.id)
                events.onNavigationPerformed.invoke()
            }

            is NavigationTarget.ShoppingListForm -> {
                bottomSheetScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }
                events.onNavigationPerformed.invoke()
            }

            null -> Unit
        }
    }
}

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
        ListOverviewScreen(
            viewState = remember { mutableStateOf(ViewState(list)) },
            events = Events(
                onShoppingListSelected = { },
                onShoppingListDeleted = { },
                onShoppingListSaved = { },
                onAddNewShoppingList = { },
                onNavigationPerformed = { },
                onToastShown = { },
                onNavigateToListDetails = { }
            )
        )
    }
}
