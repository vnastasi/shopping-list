package md.vnastasi.shoppinglist.screen.listdetails.vm

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.domain.TestData.DEFAULT_SHOPPING_LIST_NAME
import md.vnastasi.shoppinglist.domain.TestData.createShoppingItem
import md.vnastasi.shoppinglist.domain.TestData.createShoppingList
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModel.Companion.ARG_KEY_SHOPPING_LIST_ID
import md.vnastasi.shoppinglist.support.async.TestDispatchersProvider
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class ListDetailsViewModelTest {

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
        whenever(mockShoppingListRepository.findById(shoppingListId)).doReturn(flowOf(shoppingList))
        whenever(mockShoppingItemRepository.findAll(shoppingListId)).doReturn(flowOf(emptyList()))

        createViewModel(mapOf(ARG_KEY_SHOPPING_LIST_ID to shoppingListId)).screenState.test {
            awaitItem()
            assertThat(awaitItem()).isDataClassEqualTo(
                ViewState(
                    shoppingListId = shoppingListId,
                    shoppingListName = DEFAULT_SHOPPING_LIST_NAME,
                    listOfShoppingItems = persistentListOf()
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
    fun screenStateWithShoppingItems() = runTest {
        val shoppingListId = 567L
        val shoppingList = createShoppingList {
            id = shoppingListId
        }
        val shoppingItem = createShoppingItem()
        whenever(mockShoppingListRepository.findById(shoppingListId)).doReturn(flowOf(shoppingList))
        whenever(mockShoppingItemRepository.findAll(shoppingListId)).doReturn(flowOf(listOf(shoppingItem)))

        createViewModel(mapOf(ARG_KEY_SHOPPING_LIST_ID to shoppingListId)).screenState.test {
            awaitItem()
            assertThat(awaitItem()).isDataClassEqualTo(
                ViewState(
                    shoppingListId = shoppingListId,
                    shoppingListName = DEFAULT_SHOPPING_LIST_NAME,
                    listOfShoppingItems = persistentListOf(shoppingItem)
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `ShoppingItemClicked` UI event on an unchecked shopping item
        Then expect shopping item to be updated to checked state
    """
    )
    fun onShoppingListChecked() = runTest {
        val shoppingListId = 567L
        whenever(mockShoppingListRepository.findById(shoppingListId)).doReturn(flowOf(createShoppingList()))

        val shoppingItem = createShoppingItem {
            isChecked = false
        }
        whenever(mockShoppingItemRepository.findAll(shoppingListId)).doReturn(flowOf(listOf(shoppingItem)))

        val viewModel = createViewModel(mapOf(ARG_KEY_SHOPPING_LIST_ID to shoppingListId))

        viewModel.onUiEvent(UiEvent.ShoppingItemClicked(shoppingItem))
        advanceUntilIdle()

        argumentCaptor<ShoppingItem> {
            verify(mockShoppingItemRepository).update(capture())
            assertThat(firstValue).isDataClassEqualTo(shoppingItem.copy(isChecked = true))
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `ShoppingItemClicked` UI event on a checked shopping item
        Then expect shopping item to be updated to unchecked state
    """
    )
    fun onShoppingItemUnchecked() = runTest {
        val shoppingListId = 567L
        whenever(mockShoppingListRepository.findById(shoppingListId)).doReturn(flowOf(createShoppingList()))

        val shoppingItem = createShoppingItem {
            isChecked = true
        }
        whenever(mockShoppingItemRepository.findAll(shoppingListId)).doReturn(flowOf(listOf(shoppingItem)))

        val viewModel = createViewModel(mapOf(ARG_KEY_SHOPPING_LIST_ID to shoppingListId))

        viewModel.onUiEvent(UiEvent.ShoppingItemClicked(shoppingItem))
        advanceUntilIdle()

        argumentCaptor<ShoppingItem> {
            verify(mockShoppingItemRepository).update(capture())
            assertThat(firstValue).isDataClassEqualTo(shoppingItem.copy(isChecked = false))
        }
    }

    context(TestScope)
    private fun createViewModel(initialState: Map<String, Any?> = emptyMap()) = ListDetailsViewModel(
        savedStateHandle = SavedStateHandle(initialState),
        shoppingListRepository = mockShoppingListRepository,
        shoppingItemRepository = mockShoppingItemRepository,
        dispatchersProvider = TestDispatchersProvider(StandardTestDispatcher(testScheduler))
    )
}
