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
import md.vnastasi.shoppinglist.support.DomainTestData.createShoppingList
import md.vnastasi.shoppinglist.support.TestDispatchersProvider
import md.vnastasi.shoppinglist.support.state.ScreenState
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
    fun screenStateWithNoLists() = runTest {
        whenever(mockShoppingListRepository.getAvailableLists()).doReturn(flowOf(emptyList()))

        createViewModel(testScheduler).screenState.test {
            assertThat(awaitItem()).isEqualTo(ScreenState.Loading)
            assertThat(awaitItem()).isEqualTo(ScreenState.Empty)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
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
