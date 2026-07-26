package md.vnastasi.shoppinglist.screen.overview.ui

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.overview.model.Effect
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.overview.model.ShoppingListUiModel
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.NEW_SHOPPING_LIST_FAB
import md.vnastasi.shoppinglist.screen.overview.vm.OverviewViewModelSpec
import md.vnastasi.shoppinglist.screen.shared.content.AnimatedMessageContent
import md.vnastasi.shoppinglist.screen.shared.content.contentTransitionSpec
import md.vnastasi.shoppinglist.screen.shared.transition.ExtendedSharedTransitionScope
import md.vnastasi.shoppinglist.screen.shared.transition.PreviewableSharedTransitionLayout
import md.vnastasi.shoppinglist.support.annotation.ExcludeFromJacocoGeneratedReport
import md.vnastasi.shoppinglist.support.theme.AppTheme

context(extendedSharedTransitionScope: ExtendedSharedTransitionScope)
@Composable
internal fun OverviewScreen(
    viewModel: OverviewViewModelSpec,
    onNavigate: (NavigationTarget) -> Unit
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LifecycleStartEffect(viewModel.effect) {
        val job = lifecycleScope.launch {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is Effect.Navigation -> onNavigate(effect.target)
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }

    OverviewScreen(
        viewState = viewState,
        dispatchEvent = viewModel::dispatch
    )
}

context(extendedSharedTransitionScope: ExtendedSharedTransitionScope)
@Composable
private fun OverviewScreen(
    viewState: ViewState,
    dispatchEvent: (UiEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val showFloatingActionButton by remember(viewState) { derivedStateOf { viewState != ViewState.Loading } }

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
                    onClick = { dispatchEvent(UiEvent.OnAddNewShoppingList) }
                )
            }
        }
    ) { contentPaddings ->
        AnimatedContent(
            targetState = viewState,
            contentKey = { it::class },
            transitionSpec = { contentTransitionSpec }
        ) { viewState ->
            when (viewState) {
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
                    OverviewContent(
                        contentPaddings = contentPaddings,
                        list = viewState.data,
                        dispatchEvent = dispatchEvent
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
private fun ManageListFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.displayCutout).only(WindowInsetsSides.Bottom + WindowInsetsSides.End))
            .testTag(NEW_SHOPPING_LIST_FAB),
        shape = MaterialTheme.shapes.large,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.overview_btn_add_list_acc)
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
@PreviewFontScale
@Composable
private fun ListOverviewScreenPreview(
    @PreviewParameter(LisOfShoppingListUiModelsPreviewParameter::class, limit = 1) list: ImmutableList<ShoppingListUiModel>
) {
    AppTheme {
        PreviewableSharedTransitionLayout {
            OverviewScreen(
                viewState = ViewState.Ready(data = list),
                dispatchEvent = { }
            )
        }
    }
}
