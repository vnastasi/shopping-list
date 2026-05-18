package md.vnastasi.shoppinglist.screen.additems.vm

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.TestData.DEFAULT_SHOPPING_LIST_ID
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingList
import md.vnastasi.shoppinglist.domain.repository.NameSuggestionRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.additems.model.Effect
import md.vnastasi.shoppinglist.screen.additems.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.shared.toast.ToastMessage
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class AddItemsViewModelTest {

    private val mockNameSuggestionRepository = mockk<NameSuggestionRepository>(relaxUnitFun = true)
    private val mockShoppingListRepository = mockk<ShoppingListRepository>(relaxUnitFun = true)
    private val mockShoppingItemRepository = mockk<ShoppingItemRepository>(relaxUnitFun = true)

    @Test
    @DisplayName(
        """
        When handling search term value changes
        Then expect suggestion list to update
    """
    )
    fun onSearchTermChanged() = runTest {
        val searchTerm = "test"
        val suggestion = NameSuggestion(1L, "Milk")

        every { mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID) } returns flowOf(createShoppingList())
        coEvery { mockNameSuggestionRepository.findAllMatching(searchTerm) } returns flowOf(listOf(suggestion))

        val viewModel = createViewModel()
        viewModel.viewState.test {
            skipItems(1)

            viewModel.searchTermTextFieldState.setTextAndPlaceCursorAtEnd(searchTerm)

            val expectedViewState = ViewState(suggestions = persistentListOf(suggestion))
            assertThat(awaitItem()).isDataClassEqualTo(expectedViewState)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `ItemAddedToList` UI event
        Then expect item to be added to the list and a toast effect
    """
    )
    fun onItemAddedToList() = runTest {
        val name = "Milk"

        val shoppingList = createShoppingList()
        every { mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID) } returns flowOf(shoppingList)
        coEvery { mockNameSuggestionRepository.findAllMatching(any()) } returns flowOf(emptyList())

        val viewModel = createViewModel(currentSearchTermValue = "Search term")

        viewModel.effect.test {
            viewModel.dispatch(UiEvent.OnItemAddedToList(name))
            advanceUntilIdle()

            val expectedToastMessage = ToastMessage(textResourceId = R.string.toast_item_added, arguments = persistentListOf(name))
            assertThat(awaitItem()).isDataClassEqualTo(Effect.ShowToast(expectedToastMessage))
        }

        coVerify { mockShoppingItemRepository.create(eq(ShoppingItem(name = name, isChecked = false, list = shoppingList))) }
        confirmVerified(mockShoppingItemRepository)

        assertThat(viewModel.searchTermTextFieldState.text).isEmpty()
    }

    @Test
    @DisplayName(
        """
        When handling a `ItemAddedToList` UI event with an empty string
        Then expect no further action
    """
    )
    fun onEmptyItemAddedToList() = runTest {
        val shoppingList = createShoppingList()
        every { mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID) } returns flowOf(shoppingList)

        val viewModel = createViewModel(currentSearchTermValue = "Search term")
        viewModel.effect.test {
            viewModel.dispatch(UiEvent.OnItemAddedToList(""))
            advanceUntilIdle()

            expectNoEvents()
        }

        coVerify(exactly = 0) { mockShoppingItemRepository.create(any()) }
        confirmVerified(mockShoppingItemRepository, mockNameSuggestionRepository)

        assertThat(viewModel.searchTermTextFieldState.text).isEqualTo("Search term")
    }

    @Test
    @DisplayName(
        """
        When handling a `SuggestionDeleted` UI event
        Then expect suggestion to be deleted and a toast message to be displayed
    """
    )
    fun onSuggestionDeleted() = runTest {
        every { mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID) } returns flowOf(createShoppingList())
        coEvery { mockNameSuggestionRepository.findAllMatching(any()) } returns flowOf(emptyList())

        val suggestion = NameSuggestion(name = "Milk")

        val viewModel = createViewModel()
        viewModel.effect.test {
            viewModel.dispatch(UiEvent.OnSuggestionDeleted(suggestion))
            advanceUntilIdle()

            val expectedToastMessage = ToastMessage(textResourceId = R.string.toast_suggestion_removed, arguments = persistentListOf("Milk"))
            assertThat(awaitItem()).isDataClassEqualTo(Effect.ShowToast(expectedToastMessage))
        }

        viewModel.viewState.test {
            cancelAndConsumeRemainingEvents()
        }

        coVerify { mockNameSuggestionRepository.delete(suggestion) }
        confirmVerified(mockNameSuggestionRepository)
    }

    @Test
    @DisplayName(
        """
        When handling a `OnBackClicked` UI event
        Then expect navigation effect with target `BackToListDetails`
    """
    )
    fun onBackClicked() = runTest {
        every { mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID) } returns flowOf(createShoppingList())
        coEvery { mockNameSuggestionRepository.findAllMatching(any()) } returns flowOf(emptyList())

        val viewModel = createViewModel()
        viewModel.effect.test {
            viewModel.dispatch(UiEvent.OnBackClicked)
            assertThat(awaitItem()).isDataClassEqualTo(Effect.Navigation(NavigationTarget.BackToListDetails))
        }
    }

    context(scope: TestScope)
    private fun createViewModel(currentSearchTermValue: String = "") =
        AddItemsViewModel(
            shoppingListId = DEFAULT_SHOPPING_LIST_ID,
            nameSuggestionRepository = mockNameSuggestionRepository,
            shoppingItemRepository = mockShoppingItemRepository,
            shoppingListRepository = mockShoppingListRepository,
            coroutineScope = CoroutineScope(scope.coroutineContext + SupervisorJob())
        ).apply {
            searchTermTextFieldState.setTextAndPlaceCursorAtEnd(currentSearchTermValue)
        }
}
