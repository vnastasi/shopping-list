package md.vnastasi.shoppinglist.screen.listdetails.vm

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEqualTo
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.TestData.DEFAULT_SHOPPING_LIST_NAME
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingItem
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.listdetails.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.listdetails.model.UiEvent
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
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
        Then expect view state with empty list of shopping items
    """
    )
    fun viewStateWithNoShoppingItems() = runTest {
        val shoppingListId = 567L
        val shoppingList = createShoppingList {
            id = shoppingListId
        }
        every { mockShoppingListRepository.findById(shoppingListId) } returns flowOf(shoppingList)
        every { mockShoppingItemRepository.findAll(shoppingListId) } returns flowOf(emptyList())

        createViewModel(shoppingListId).viewState.test {
            assertThat(awaitItem()).isEqualTo(ViewState.Loading)
            assertThat(awaitItem()).isDataClassEqualTo(
                ViewState.Empty(
                    shoppingListId = shoppingListId,
                    shoppingListName = DEFAULT_SHOPPING_LIST_NAME,
                    navigationTarget = null
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
        Then expect view state with non-empty list of shopping items
    """
    )
    fun viewStateWithShoppingItems() = runTest {
        val shoppingListId = 567L
        val shoppingList = createShoppingList {
            id = shoppingListId
        }
        val shoppingItem = createShoppingItem()
        every { mockShoppingListRepository.findById(shoppingListId) } returns flowOf(shoppingList)
        every { mockShoppingItemRepository.findAll(shoppingListId) } returns flowOf(listOf(shoppingItem))

        createViewModel(shoppingListId).viewState.test {
            assertThat(awaitItem()).isEqualTo(ViewState.Loading)
            assertThat(awaitItem()).isDataClassEqualTo(
                ViewState.Ready(
                    shoppingListId = shoppingListId,
                    shoppingListName = DEFAULT_SHOPPING_LIST_NAME,
                    listOfShoppingItems = persistentListOf(shoppingItem),
                    navigationTarget = null
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
    fun onShoppingItemChecked() = runTest {
        val shoppingListId = 567L
        every { mockShoppingListRepository.findById(shoppingListId) } returns flowOf(createShoppingList())

        val shoppingItem = createShoppingItem {
            isChecked = false
        }
        every { mockShoppingItemRepository.findAll(shoppingListId) } returns flowOf(listOf(shoppingItem))

        val shoppingItemSlot = slot<ShoppingItem>()
        coEvery { mockShoppingItemRepository.update(capture(shoppingItemSlot)) } returns Unit

        val viewModel = createViewModel(shoppingListId)

        viewModel.dispatch(UiEvent.OnItemClicked(shoppingItem))
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

        val viewModel = createViewModel(shoppingListId)

        viewModel.dispatch(UiEvent.OnItemClicked(shoppingItem))
        advanceUntilIdle()

        assertThat(shoppingItemSlot.captured).isDataClassEqualTo(shoppingItem.copy(isChecked = false))
    }

    @Test
    @DisplayName(
        """
        When handling a `ShoppingItemDeleted` UI event on a shopping item
        Then expect shopping item to be deleted
    """
    )
    fun onShoppingItemDeleted() = runTest {
        val shoppingListId = 567L
        every { mockShoppingListRepository.findById(shoppingListId) } returns flowOf(createShoppingList())

        val shoppingItem = createShoppingItem()
        every { mockShoppingItemRepository.findAll(shoppingListId) } returns flowOf(listOf(shoppingItem))

        val shoppingItemSlot = slot<ShoppingItem>()
        coEvery { mockShoppingItemRepository.delete(capture(shoppingItemSlot)) } returns Unit

        val viewModel = createViewModel(shoppingListId)

        viewModel.dispatch(UiEvent.OnItemDeleted(shoppingItem))
        advanceUntilIdle()

        assertThat(shoppingItemSlot.captured).isDataClassEqualTo(shoppingItem)
    }

    @Test
    @DisplayName(
        """
        When handling an `OnAddItemsClicked` UI event
        Then expect navigation target to be set to `AddItems`
    """
    )
    fun onAddItemsClicked() = runTest {
        val shoppingListId = 567L
        every { mockShoppingListRepository.findById(shoppingListId) } returns flowOf(createShoppingList { id = shoppingListId })
        every { mockShoppingItemRepository.findAll(shoppingListId) } returns flowOf(emptyList())

        val viewModel = createViewModel(shoppingListId)
        viewModel.viewState.test {
            viewModel.dispatch(UiEvent.OnAddItemsClicked)
            advanceUntilIdle()

            assertThat(expectMostRecentItem().navigationTarget).isEqualTo(NavigationTarget.AddItems(shoppingListId))

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        When handling an `OnBackClicked` UI event
        Then expect navigation target to be set to `BackToOverview`
    """
    )
    fun onBackClicked() = runTest {
        val shoppingListId = 567L
        every { mockShoppingListRepository.findById(shoppingListId) } returns flowOf(createShoppingList { id = shoppingListId })
        every { mockShoppingItemRepository.findAll(shoppingListId) } returns flowOf(emptyList())

        val viewModel = createViewModel(shoppingListId)
        viewModel.viewState.test {
            viewModel.dispatch(UiEvent.OnBackClicked)
            advanceUntilIdle()

            assertThat(expectMostRecentItem().navigationTarget).isEqualTo(NavigationTarget.BackToOverview)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        When handling an `OnNavigationConsumed` UI event
        Then expect navigation target to be set to `null`
    """
    )
    fun onNavigationConsumed() = runTest {
        val shoppingListId = 567L
        every { mockShoppingListRepository.findById(shoppingListId) } returns flowOf(createShoppingList { id = shoppingListId })
        every { mockShoppingItemRepository.findAll(shoppingListId) } returns flowOf(emptyList())

        val viewModel = createViewModel(shoppingListId)
        viewModel.viewState.test {
            viewModel.dispatch(UiEvent.OnBackClicked)
            advanceUntilIdle()
            assertThat(expectMostRecentItem().navigationTarget).isEqualTo(NavigationTarget.BackToOverview)

            viewModel.dispatch(UiEvent.OnNavigationConsumed)
            advanceUntilIdle()
            assertThat(expectMostRecentItem().navigationTarget).isEqualTo(null)

            cancelAndConsumeRemainingEvents()
        }
    }

    context(scope: TestScope)
    private fun createViewModel(shoppingListId: Long) =
        ListDetailsViewModel(
            shoppingListId = shoppingListId,
            shoppingListRepository = mockShoppingListRepository,
            shoppingItemRepository = mockShoppingItemRepository,
            coroutineScope = CoroutineScope(scope.coroutineContext + SupervisorJob())
        )
}
