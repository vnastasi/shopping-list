package md.vnastasi.shoppinglist.screen.overview.vm

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEqualTo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingList
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingListDetails
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.overview.model.Effect
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.overview.model.ShoppingListUiModel
import md.vnastasi.shoppinglist.screen.overview.model.SwipeToRevealState
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class OverviewViewModelTest {

    private val mockShoppingListRepository = mockk<ShoppingListRepository>(relaxUnitFun = true)

    @Test
    @DisplayName(
        """
        Given repository returns no shopping lists
        When creating overview screen state
        Then expect view state with empty list
    """
    )
    fun viewStateWithNoLists() = runTest {
        every { mockShoppingListRepository.findAll() } returns flowOf(emptyList())

        createViewModel().viewState.test {
            assertThat(awaitItem()).isEqualTo(ViewState.Loading)
            assertThat(awaitItem()).isEqualTo(ViewState.Empty)
        }
    }

    @Test
    @DisplayName(
        """
        Given repository returns shopping lists
        When creating list overview screen state
        Then expect view state with a list of shopping lists
    """
    )
    fun viewStateWithLists() = runTest {
        val shoppingListDetails = createShoppingListDetails()
        every { mockShoppingListRepository.findAll() } returns flowOf(listOf(shoppingListDetails))

        val expectedShoppingListUiModel = ShoppingListUiModel(shoppingListDetails, SwipeToRevealState.Content)
        createViewModel().viewState.test {
            assertThat(awaitItem()).isEqualTo(ViewState.Loading)
            assertThat(awaitItem()).isDataClassEqualTo(ViewState.Ready(data = persistentListOf(expectedShoppingListUiModel)))
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `OnShoppingListDeleted` UI event
        Then expect repository to delete said shopping list
    """
    )
    fun onShoppingListDeleted() = runTest {
        val shoppingListDetails = createShoppingListDetails()
        every { mockShoppingListRepository.findAll() } returns flowOf(listOf(shoppingListDetails))

        val viewModel = createViewModel()
        val event = UiEvent.OnShoppingListDeleted(ShoppingListUiModel(shoppingListDetails, SwipeToRevealState.Content))
        viewModel.dispatch(event)
        advanceUntilIdle()

        coVerify { mockShoppingListRepository.delete(createShoppingList()) }
        coVerify { mockShoppingListRepository.findAll() }
        confirmVerified(mockShoppingListRepository)
    }

    @Test
    @DisplayName(
        """
        When handling a `OnShoppingListsReordered` UI event
        Then expect repository to reorder shopping lists
    """
    )
    fun onShoppingListsReordered() = runTest {
        val shoppingListDetails1 = createShoppingListDetails {
            id = 1L
            name = "L1"
            position = 0L
        }
        val shoppingListDetails2 = createShoppingListDetails {
            id = 2L
            name = "L2"
            position = 1L
        }
        val shoppingListDetails3 = createShoppingListDetails {
            id = 3L
            name = "L3"
            position = 2L
        }

        every { mockShoppingListRepository.findAll() } returns flowOf(listOf(shoppingListDetails1, shoppingListDetails2, shoppingListDetails3))

        val reorderedListSlot = slot<List<ShoppingList>>()
        coEvery { mockShoppingListRepository.update(capture(reorderedListSlot)) } returns Unit

        val viewModel = createViewModel()
        val event = UiEvent.OnShoppingListsReordered(
            listOf(
                ShoppingListUiModel(shoppingListDetails3, SwipeToRevealState.Content),
                ShoppingListUiModel(shoppingListDetails1, SwipeToRevealState.Content),
                ShoppingListUiModel(shoppingListDetails2, SwipeToRevealState.Content)
            )
        )
        viewModel.dispatch(event)
        advanceUntilIdle()

        assertThat(reorderedListSlot.captured).containsExactly(
            createShoppingList {
                id = 3L
                name = "L3"
                position = 0L
            },
            createShoppingList {
                id = 1L
                name = "L1"
                position = 1L
            },
            createShoppingList {
                id = 2L
                name = "L2"
                position = 2L
            }
        )
    }

    @Test
    @DisplayName(
        """
        When handling a `OnAddNewShoppingList` UI event
        Then expect navigation effect with target `AddOrEditList` with no ID supplied
    """
    )
    fun onAddNewShoppingList() = runTest {
        val shoppingListDetails = createShoppingListDetails()
        every { mockShoppingListRepository.findAll() } returns flowOf(listOf(shoppingListDetails))

        val viewModel = createViewModel()

        viewModel.effect.test {
            viewModel.dispatch(UiEvent.OnAddNewShoppingList)
            advanceUntilIdle()

            val expectedEffect = Effect.Navigation(NavigationTarget.AddOrEditList(null))
            assertThat(expectMostRecentItem()).isDataClassEqualTo(expectedEffect)
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `OnShoppingListEdited` UI event
        Then expect navigation effect with target `AddOrEditList` with shopping list ID supplied
    """
    )
    fun onShoppingListEdited() = runTest {
        val shoppingListDetails = createShoppingListDetails()
        every { mockShoppingListRepository.findAll() } returns flowOf(listOf(shoppingListDetails))

        val viewModel = createViewModel()

        viewModel.effect.test {
            val event = UiEvent.OnShoppingListEdited(ShoppingListUiModel(shoppingListDetails, SwipeToRevealState.Content))
            viewModel.dispatch(event)
            advanceUntilIdle()

            val expectedEffect = Effect.Navigation(NavigationTarget.AddOrEditList(shoppingListDetails.id))
            assertThat(expectMostRecentItem()).isDataClassEqualTo(expectedEffect)
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `OnShoppingListSelected` UI event
        Then expect navigation effect with target `ListDetails` with shopping list ID supplied
    """
    )
    fun onShoppingListSelected() = runTest {
        val shoppingListDetails = createShoppingListDetails()
        every { mockShoppingListRepository.findAll() } returns flowOf(listOf(shoppingListDetails))

        val viewModel = createViewModel()
        viewModel.effect.test {
            val event = UiEvent.OnShoppingListSelected(ShoppingListUiModel(shoppingListDetails, SwipeToRevealState.Content))
            viewModel.dispatch(event)
            advanceUntilIdle()

            val expectedEffect = Effect.Navigation(NavigationTarget.ListDetails(shoppingListDetails.id))
            assertThat(expectMostRecentItem()).isDataClassEqualTo(expectedEffect)
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `OnSwipeToRevealStateChanged` UI event
        Then expect swipe to reveal state to change for the affected shopping list
    """
    )
    fun onSwipeToRevealStateChanged() = runTest {
        val shoppingListDetails = createShoppingListDetails()
        every { mockShoppingListRepository.findAll() } returns flowOf(listOf(shoppingListDetails))

        val viewModel = createViewModel()
        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(ViewState.Loading)

            val originalShoppingListUiModel = ShoppingListUiModel(shoppingListDetails, SwipeToRevealState.Content)
            assertThat(awaitItem()).isEqualTo(ViewState.Ready(persistentListOf(originalShoppingListUiModel)))

            val event = UiEvent.OnSwipeToRevealStateChanged(originalShoppingListUiModel, SwipeToRevealState.Actions)
            viewModel.dispatch(event)
            advanceUntilIdle()

            val updatedShoppingListUiModel = ShoppingListUiModel(shoppingListDetails, SwipeToRevealState.Actions)
            assertThat(awaitItem()).isEqualTo(ViewState.Ready(persistentListOf(updatedShoppingListUiModel)))
        }

        viewModel.effect.test {
            val event = UiEvent.OnShoppingListSelected(ShoppingListUiModel(shoppingListDetails, SwipeToRevealState.Content))
            viewModel.dispatch(event)
            advanceUntilIdle()

            val expectedEffect = Effect.Navigation(NavigationTarget.ListDetails(shoppingListDetails.id))
            assertThat(expectMostRecentItem()).isDataClassEqualTo(expectedEffect)
        }
    }

    context(scope: TestScope)
    private fun createViewModel() = OverviewViewModel(
        shoppingListRepository = mockShoppingListRepository,
        savedStateHandle = SavedStateHandle(),
        coroutineScope = CoroutineScope(scope.coroutineContext + SupervisorJob() + StandardTestDispatcher(scope.testScheduler))
    )
}
