package md.vnastasi.shoppinglist.screen.listdetails

import androidx.lifecycle.SavedStateHandle
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
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.listdetails.ListDetailsViewModel.Companion.ARG_KEY_SHOPPING_LIST_ID
import md.vnastasi.shoppinglist.support.DomainTestData.DEFAULT_SHOPPING_LIST_NAME
import md.vnastasi.shoppinglist.support.DomainTestData.createShoppingItem
import md.vnastasi.shoppinglist.support.DomainTestData.createShoppingList
import md.vnastasi.shoppinglist.support.TestDispatchersProvider
import md.vnastasi.shoppinglist.support.state.ScreenState
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ListDetailsViewModelTest {

    private val mockShoppingListRepository = mock<ShoppingListRepository>()
    private val mockShoppingItemRepository = mock<ShoppingItemRepository>()

    @Test
    @DisplayName(
        """
        Given repository returns no shopping items for list 567
        When creating list details screen state for list 567
        Then expect ScreenState.Ready with empty list of shopping items
    """
    )
    fun screenStateWithNoShoppingItems() = runTest {
        val shoppingListId = 567L
        val shoppingList = createShoppingList {
            id = shoppingListId
        }
        whenever(mockShoppingListRepository.getListById(shoppingListId)).doReturn(flowOf(shoppingList))
        whenever(mockShoppingItemRepository.getAllItems(shoppingListId)).doReturn(flowOf(emptyList()))

        createViewModel(testScheduler, mapOf(ARG_KEY_SHOPPING_LIST_ID to shoppingListId)).screenState.test {
            assertThat(awaitItem()).isEqualTo(ScreenState.Loading)
            assertThat(awaitItem()).isDataClassEqualTo(
                ScreenState.Ready(
                    ListDetails(
                        id = shoppingListId,
                        name = DEFAULT_SHOPPING_LIST_NAME,
                        listOfShoppingItems = persistentListOf()
                    )
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        Given repository returns shopping items for list 567
        When creating list details screen state for list 567
        Then expect ScreenState.Ready with non-empty list of shopping items
    """
    )
    fun screenState() = runTest {
        val shoppingListId = 567L
        val shoppingList = createShoppingList {
            id = shoppingListId
        }
        val shoppingItem = createShoppingItem()
        whenever(mockShoppingListRepository.getListById(shoppingListId)).doReturn(flowOf(shoppingList))
        whenever(mockShoppingItemRepository.getAllItems(shoppingListId)).doReturn(flowOf(listOf(shoppingItem)))

        createViewModel(testScheduler, mapOf(ARG_KEY_SHOPPING_LIST_ID to shoppingListId)).screenState.test {
            assertThat(awaitItem()).isEqualTo(ScreenState.Loading)
            assertThat(awaitItem()).isDataClassEqualTo(
                ScreenState.Ready(
                    ListDetails(
                        id = shoppingListId,
                        name = DEFAULT_SHOPPING_LIST_NAME,
                        listOfShoppingItems = persistentListOf(shoppingItem)
                    )
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        When selecting a previously unselected shopping item
        Then expect shopping item to be updated to checked state
    """
    )
    fun onShoppingListItemChecked() = runTest {
        val shoppingListId = 567L
        whenever(mockShoppingListRepository.getListById(shoppingListId)).doReturn(flowOf(createShoppingList()))

        val shoppingItem = createShoppingItem {
            isChecked = false
        }
        whenever(mockShoppingItemRepository.getAllItems(shoppingListId)).doReturn(flowOf(listOf(shoppingItem)))

        val viewModel = createViewModel(testScheduler, mapOf(ARG_KEY_SHOPPING_LIST_ID to shoppingListId))

        viewModel.onUiEvent(UiEvent.OnShoppingListItemClicked(shoppingItem))
        advanceUntilIdle()

        argumentCaptor<ShoppingItem> {
            verify(mockShoppingItemRepository).update(capture())
            assertThat(firstValue).isDataClassEqualTo(shoppingItem.copy(isChecked = true))
        }
    }

    @Test
    @DisplayName(
        """
        When deselecting a previously selected shopping item
        Then expect shopping item to be updated to unchecked state
    """
    )
    fun onShoppingListItemUnchecked() = runTest {
        val shoppingListId = 567L
        whenever(mockShoppingListRepository.getListById(shoppingListId)).doReturn(flowOf(createShoppingList()))

        val shoppingItem = createShoppingItem {
            isChecked = true
        }
        whenever(mockShoppingItemRepository.getAllItems(shoppingListId)).doReturn(flowOf(listOf(shoppingItem)))

        val viewModel = createViewModel(testScheduler, mapOf(ARG_KEY_SHOPPING_LIST_ID to shoppingListId))

        viewModel.onUiEvent(UiEvent.OnShoppingListItemClicked(shoppingItem))
        advanceUntilIdle()

        argumentCaptor<ShoppingItem> {
            verify(mockShoppingItemRepository).update(capture())
            assertThat(firstValue).isDataClassEqualTo(shoppingItem.copy(isChecked = false))
        }
    }

    private fun createViewModel(
        scheduler: TestCoroutineScheduler,
        initialState: Map<String, Any?> = emptyMap()
    ) = ListDetailsViewModel(
        savedStateHandle = SavedStateHandle(initialState),
        shoppingListRepository = mockShoppingListRepository,
        shoppingItemRepository = mockShoppingItemRepository,
        dispatchersProvider = TestDispatchersProvider(StandardTestDispatcher(scheduler))
    )
}