package md.vnastasi.shoppinglist.screen.listdetails.vm

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.TestData.DEFAULT_SHOPPING_LIST_NAME
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingItem
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModel.Companion.ARG_KEY_SHOPPING_LIST_ID
import md.vnastasi.shoppinglist.support.async.TestDispatchersProvider
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ListDetailsViewModelTest {

    private val mockShoppingListRepository = mockk<ShoppingListRepository>(relaxUnitFun = true)
    private val mockShoppingItemRepository = mockk<ShoppingItemRepository>(relaxUnitFun = true)

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
        every { mockShoppingListRepository.findById(shoppingListId) } returns flowOf(shoppingList)
        every { mockShoppingItemRepository.findAll(shoppingListId) } returns flowOf(emptyList())

        createViewModel(testScheduler, mapOf(ARG_KEY_SHOPPING_LIST_ID to shoppingListId)).screenState.test {
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
        every { mockShoppingListRepository.findById(shoppingListId) } returns flowOf(shoppingList)
        every { mockShoppingItemRepository.findAll(shoppingListId) } returns flowOf(listOf(shoppingItem))

        createViewModel(testScheduler, mapOf(ARG_KEY_SHOPPING_LIST_ID to shoppingListId)).screenState.test {
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
        every { mockShoppingListRepository.findById(shoppingListId) } returns flowOf(createShoppingList())

        val shoppingItem = createShoppingItem {
            isChecked = false
        }
        every { mockShoppingItemRepository.findAll(shoppingListId) } returns flowOf(listOf(shoppingItem))

        val shoppingItemSlot = slot<ShoppingItem>()
        coEvery { mockShoppingItemRepository.update(capture(shoppingItemSlot)) } returns Unit

        val viewModel = createViewModel(testScheduler, mapOf(ARG_KEY_SHOPPING_LIST_ID to shoppingListId))

        viewModel.onUiEvent(UiEvent.ShoppingItemClicked(shoppingItem))
        advanceUntilIdle()

        assertThat(shoppingItemSlot.captured).isDataClassEqualTo(shoppingItem.copy(isChecked = true))
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
        every { mockShoppingListRepository.findById(shoppingListId) } returns flowOf(createShoppingList())

        val shoppingItem = createShoppingItem {
            isChecked = true
        }
        every { mockShoppingItemRepository.findAll(shoppingListId) } returns flowOf(listOf(shoppingItem))

        val shoppingItemSlot = slot<ShoppingItem>()
        coEvery { mockShoppingItemRepository.update(capture(shoppingItemSlot)) } returns Unit

        val viewModel = createViewModel(testScheduler, mapOf(ARG_KEY_SHOPPING_LIST_ID to shoppingListId))

        viewModel.onUiEvent(UiEvent.ShoppingItemClicked(shoppingItem))
        advanceUntilIdle()

        assertThat(shoppingItemSlot.captured).isDataClassEqualTo(shoppingItem.copy(isChecked = false))
    }

    private fun createViewModel(
        testScheduler: TestCoroutineScheduler,
        initialState: Map<String, Any?> = emptyMap()
    ) = ListDetailsViewModel(
        savedStateHandle = SavedStateHandle(initialState),
        shoppingListRepository = mockShoppingListRepository,
        shoppingItemRepository = mockShoppingItemRepository,
        dispatchersProvider = TestDispatchersProvider(StandardTestDispatcher(testScheduler))
    )
}
