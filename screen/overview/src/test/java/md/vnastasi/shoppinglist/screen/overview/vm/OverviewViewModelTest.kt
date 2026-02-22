package md.vnastasi.shoppinglist.screen.overview.vm

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
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
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
            assertThat(awaitItem()).isEqualTo(ViewState.Empty(navigationTarget = null))
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

        createViewModel().viewState.test {
            assertThat(awaitItem()).isEqualTo(ViewState.Loading)
            assertThat(awaitItem()).isDataClassEqualTo(ViewState.Ready(data = persistentListOf(shoppingListDetails), navigationTarget = null))
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
        viewModel.dispatch(UiEvent.OnShoppingListDeleted(shoppingListDetails))
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
        viewModel.dispatch(UiEvent.OnShoppingListsReordered(listOf(shoppingListDetails3, shoppingListDetails1, shoppingListDetails2)))
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
        Then expect navigation target to be set to `AddOrEditList` with no ID supplied
    """
    )
    fun onAddNewShoppingList() = runTest {
        val shoppingListDetails = createShoppingListDetails()
        every { mockShoppingListRepository.findAll() } returns flowOf(listOf(shoppingListDetails))

        val viewModel = createViewModel()

        viewModel.viewState.test {
            viewModel.dispatch(UiEvent.OnAddNewShoppingList)
            advanceUntilIdle()

            val expectedViewState = ViewState.Ready(data = persistentListOf(shoppingListDetails), navigationTarget = NavigationTarget.AddOrEditList(null))
            assertThat(expectMostRecentItem()).isDataClassEqualTo(expectedViewState)
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `OnShoppingListEdited` UI event
        Then expect navigation target to be set to `AddOrEditList` with shopping list ID supplied
    """
    )
    fun onShoppingListEdited() = runTest {
        val shoppingListDetails = createShoppingListDetails()
        every { mockShoppingListRepository.findAll() } returns flowOf(listOf(shoppingListDetails))

        val viewModel = createViewModel()

        viewModel.viewState.test {
            viewModel.dispatch(UiEvent.OnShoppingListEdited(shoppingListDetails))
            advanceUntilIdle()

            val expectedViewState = ViewState.Ready(data = persistentListOf(shoppingListDetails), navigationTarget = NavigationTarget.AddOrEditList(shoppingListDetails.id))
            assertThat(expectMostRecentItem()).isDataClassEqualTo(expectedViewState)
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `OnShoppingListSelected` UI event
        Then expect navigation target to be set to `ListDetails` with shopping list ID supplied
    """
    )
    fun onShoppingListSelected() = runTest {
        val shoppingListDetails = createShoppingListDetails()
        every { mockShoppingListRepository.findAll() } returns flowOf(listOf(shoppingListDetails))

        val viewModel = createViewModel()
        viewModel.viewState.test {
            viewModel.dispatch(UiEvent.OnShoppingListSelected(shoppingListDetails))
            advanceUntilIdle()

            val expectedViewState = ViewState.Ready(data = persistentListOf(shoppingListDetails), navigationTarget = NavigationTarget.ListDetails(shoppingListDetails.id))
            assertThat(expectMostRecentItem()).isDataClassEqualTo(expectedViewState)
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `OnNavigationConsumed` UI event
        Then expect navigation target to be set to `null`
    """
    )
    fun onNavigationConsumed() = runTest {
        val shoppingListDetails = createShoppingListDetails()
        every { mockShoppingListRepository.findAll() } returns flowOf(listOf(shoppingListDetails))

        val viewModel = createViewModel()

        viewModel.viewState.test {
            // First, trigger an event to set a navigation target
            viewModel.dispatch(UiEvent.OnAddNewShoppingList)
            advanceUntilIdle()
            assertThat(expectMostRecentItem()).isDataClassEqualTo(ViewState.Ready(data = persistentListOf(shoppingListDetails), navigationTarget = NavigationTarget.AddOrEditList(null)))

            // Then, consume it
            viewModel.dispatch(UiEvent.OnNavigationConsumed)
            advanceUntilIdle()

            val expectedViewState = ViewState.Ready(data = persistentListOf(shoppingListDetails), navigationTarget = null)
            assertThat(expectMostRecentItem()).isDataClassEqualTo(expectedViewState)
        }
    }

    context(scope: TestScope)
    private fun createViewModel() = OverviewViewModel(
        shoppingListRepository = mockShoppingListRepository,
        coroutineScope = CoroutineScope(scope.coroutineContext + SupervisorJob() + StandardTestDispatcher(scope.testScheduler))
    )
}
