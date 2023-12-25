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
import md.vnastasi.shoppinglist.R
import md.vnastasi.shoppinglist.domain.TestData.createShoppingList
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.support.async.TestDispatchersProvider
import md.vnastasi.shoppinglist.support.ui.toast.ToastMessage
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
        whenever(mockShoppingListRepository.findAll()).doReturn(flowOf(emptyList()))

        createViewModel(testScheduler).screenState.test {
            assertThat(awaitItem()).isDataClassEqualTo(ViewState(persistentListOf()))
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
    fun screenStateWithLists() = runTest {
        val shoppingList = createShoppingList()
        whenever(mockShoppingListRepository.findAll()).doReturn(flowOf(listOf(shoppingList)))

        createViewModel(testScheduler).screenState.test {
            assertThat(awaitItem()).isDataClassEqualTo(ViewState.Init)
            assertThat(awaitItem()).isDataClassEqualTo(ViewState(persistentListOf(shoppingList)))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `AddNewShoppingList` UI event
        Then expect a navigation target of type 'ShoppingListForm'
    """
    )
    fun onAddNewShoppingList() = runTest {
        whenever(mockShoppingListRepository.findAll()).doReturn(flowOf(emptyList()))

        val viewModel = createViewModel(testScheduler)
        viewModel.onUiEvent(UiEvent.AddNewShoppingList)

        viewModel.screenState.test {
            skipItems(1)

            assertThat(awaitItem()).isDataClassEqualTo(
                ViewState(shoppingLists = persistentListOf(), navigationTarget = NavigationTarget.ShoppingListForm)
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `ShoppingListSaved` UI event with a shopping list name
        Then expect repository to create a new shopping list with said name
    """
    )
    fun onShoppingListSaved() = runTest {
        val shoppingListName = "new list here"
        whenever(mockShoppingListRepository.findAll()).doReturn(flowOf(emptyList()))

        val viewModel = createViewModel(testScheduler)
        viewModel.onUiEvent(UiEvent.ShoppingListSaved(shoppingListName))
        advanceUntilIdle()

        argumentCaptor<ShoppingList> {
            verify(mockShoppingListRepository).create(capture())
            assertThat(firstValue.name).isEqualTo(shoppingListName)
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `ShoppingListSelected` UI event with a shopping list ID
        Then expect a navigation target of type 'ShoppingListDetails' with said ID
    """
    )
    fun onShoppingListSelected() = runTest {
        val shoppingList = createShoppingList {
            id = 6578L
        }
        whenever(mockShoppingListRepository.findAll()).doReturn(flowOf(emptyList()))

        val viewModel = createViewModel(testScheduler)
        viewModel.onUiEvent(UiEvent.ShoppingListSelected(shoppingList))

        viewModel.screenState.test {
            skipItems(1)

            assertThat(awaitItem()).isDataClassEqualTo(
                ViewState(shoppingLists = persistentListOf(), navigationTarget = NavigationTarget.ShoppingListDetails(6578L))
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `ShoppingListDeleted` UI event
        Then expect repository to delete said shopping list
    """
    )
    fun onShoppingListDeleted() = runTest {
        val shoppingList = createShoppingList()
        whenever(mockShoppingListRepository.findAll()).doReturn(flowOf(emptyList()))

        val viewModel = createViewModel(testScheduler)
        viewModel.onUiEvent(UiEvent.ShoppingListDeleted(shoppingList))
        advanceUntilIdle()

        verify(mockShoppingListRepository).delete(eq(shoppingList))
    }

    @Test
    @DisplayName(
        """
        When handling a `NavigationPerformed` UI event
        Then expect navigation target to be cleared
    """
    )
    fun onNavigationPerformed() = runTest {
        whenever(mockShoppingListRepository.findAll()).doReturn(flowOf(emptyList()))

        val viewModel = createViewModel(testScheduler)

        viewModel.screenState.test {
            assertThat(awaitItem()).isDataClassEqualTo(
                ViewState(shoppingLists = persistentListOf(), navigationTarget = null)
            )

            viewModel.onUiEvent(UiEvent.AddNewShoppingList)
            assertThat(awaitItem()).isDataClassEqualTo(
                ViewState(shoppingLists = persistentListOf(), navigationTarget = NavigationTarget.ShoppingListForm)
            )

            viewModel.onUiEvent(UiEvent.NavigationPerformed)
            assertThat(awaitItem()).isDataClassEqualTo(
                ViewState(shoppingLists = persistentListOf(), navigationTarget = null)
            )

            cancelAndConsumeRemainingEvents()
        }
    }


    @Test
    @DisplayName(
        """
        When handling a `ToastShown` UI event
        Then expect toast message to be cleared
    """
    )
    fun onToastShown() = runTest {
        whenever(mockShoppingListRepository.findAll()).doReturn(flowOf(emptyList()))

        val viewModel = createViewModel(testScheduler)

        viewModel.screenState.test {
            assertThat(awaitItem()).isDataClassEqualTo(
                ViewState(shoppingLists = persistentListOf(), navigationTarget = null)
            )

            viewModel.onUiEvent(UiEvent.ShoppingListSaved("test"))
            assertThat(awaitItem()).isDataClassEqualTo(
                ViewState(shoppingLists = persistentListOf(), toastMessage = ToastMessage(textResourceId = R.string.toast_list_created, arguments = persistentListOf("test")))
            )

            viewModel.onUiEvent(UiEvent.ToastShown)
            assertThat(awaitItem()).isDataClassEqualTo(
                ViewState(shoppingLists = persistentListOf(), toastMessage = null)
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    private fun createViewModel(scheduler: TestCoroutineScheduler) = ListOverviewViewModel(
        shoppingListRepository = mockShoppingListRepository,
        dispatchersProvider = TestDispatchersProvider(StandardTestDispatcher(scheduler))
    )
}
