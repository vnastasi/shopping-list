package md.vnastasi.shoppinglist.screen.listoverview

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEqualTo
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.support.testdata.DomainTestData.createShoppingList
import md.vnastasi.shoppinglist.support.async.TestDispatchersProvider
import md.vnastasi.shoppinglist.support.state.ScreenState
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ListOverviewViewModelTest {

    private val mockShoppingListRepository = mock<ShoppingListRepository>()

    @Test
    @DisplayName(
        """
        Given repository returns no shopping lists
        When creating list overview screen state
        Then expect ScreenState.Empty
    """
    )
    fun screenStateWithNoLists() = runTest {
        whenever(mockShoppingListRepository.getAvailableLists()).doReturn(flowOf(emptyList()))

        createViewModel(testScheduler).screenState.test {
            assertThat(awaitItem()).isEqualTo(ScreenState.Loading)
            assertThat(awaitItem()).isEqualTo(ScreenState.Empty)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        Given repository returns shopping lists
        When creating list overview screen state
        Then expect ScreenState.Ready with a list of shopping lists
    """
    )
    fun screenState() = runTest {
        val shoppingList = createShoppingList()
        whenever(mockShoppingListRepository.getAvailableLists()).doReturn(flowOf(listOf(shoppingList)))

        createViewModel(testScheduler).screenState.test {
            assertThat(awaitItem()).isEqualTo(ScreenState.Loading)
            assertThat(awaitItem()).isDataClassEqualTo(ScreenState.Ready(persistentListOf(shoppingList)))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        When clicking on 'Add new shopping list' button
        Then expect a UI navigation event of type 'ShoppingListForm'
    """
    )
    fun onAddNewShoppingListClicked() = runTest {
        whenever(mockShoppingListRepository.getAvailableLists()).doReturn(flowOf(emptyList()))

        val viewModel = createViewModel(testScheduler)
        viewModel.onUiEvent(UiEvent.OnAddNewShoppingListClicked)

        viewModel.navigationTarget.test {
            assertThat(awaitItem()).isEqualTo(NavigationTarget.ShoppingListForm)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        When saving a new shopping list with a given name
        Then expect repository to create a new shopping list with said name
    """
    )
    fun onSaveNewShoppingList() = runTest {
        val shoppingListName = "new list here"
        whenever(mockShoppingListRepository.getAvailableLists()).doReturn(flowOf(emptyList()))

        val viewModel = createViewModel(testScheduler)
        viewModel.onUiEvent(UiEvent.OnSaveNewShoppingList(shoppingListName))
        advanceUntilIdle()

        argumentCaptor<ShoppingList> {
            verify(mockShoppingListRepository).create(capture())
            assertThat(firstValue.name).isEqualTo(shoppingListName)
        }
    }

    @Test
    @DisplayName(
        """
        When clicking on a shopping list item
        Then expect a UI navigation event of type 'ShoppingListDetails' with list ID
    """
    )
    fun onListItemClicked() = runTest {
        val shoppingList = createShoppingList {
            id = 6578L
        }
        whenever(mockShoppingListRepository.getAvailableLists()).doReturn(flowOf(emptyList()))

        val viewModel = createViewModel(testScheduler)
        viewModel.onUiEvent(UiEvent.OnShoppingListItemClicked(shoppingList))

        viewModel.navigationTarget.test {
            assertThat(awaitItem()).isDataClassEqualTo(NavigationTarget.ShoppingListDetails(6578L))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        When deleting a shopping list
        Then expect repository to delete said shopping list
    """
    )
    fun onListItemDeleted() = runTest {
        val shoppingList = createShoppingList()
        whenever(mockShoppingListRepository.getAvailableLists()).doReturn(flowOf(emptyList()))

        val viewModel = createViewModel(testScheduler)
        viewModel.onUiEvent(UiEvent.OnShoppingListItemDeleted(shoppingList))
        advanceUntilIdle()

        verify(mockShoppingListRepository).delete(eq(shoppingList))
    }

    private fun createViewModel(scheduler: TestCoroutineScheduler) = ListOverviewViewModel(
        shoppingListRepository = mockShoppingListRepository,
        dispatchersProvider = TestDispatchersProvider(StandardTestDispatcher(scheduler))
    )
}
