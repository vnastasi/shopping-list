package md.vnastasi.shoppinglist.screen.additems.vm

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
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
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.TestData.DEFAULT_SHOPPING_LIST_ID
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingList
import md.vnastasi.shoppinglist.domain.repository.NameSuggestionRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.res.R
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
        When handling a `SearchTermChanged` UI event
        Then expect suggestion list to update
    """
    )
    fun onSearchTermChanged() = runTest {
        val searchTerm = "test"
        val suggestion = NameSuggestion(1L, "Milk")

        every { mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID) } returns flowOf(createShoppingList())
        coEvery { mockNameSuggestionRepository.findAllMatching(searchTerm) } returns listOf(suggestion)

        val viewModel = createViewModel(currentSearchTermValue = searchTerm)
        viewModel.viewState.test {
            skipItems(1)

            viewModel.onUiEvent(UiEvent.SearchTermChanged)
            assertThat(awaitItem()).isDataClassEqualTo(ViewState(suggestions = persistentListOf(suggestion), toastMessage = null))

            cancelAndConsumeRemainingEvents()
        }

        coVerify { mockNameSuggestionRepository.findAllMatching(searchTerm) }
        confirmVerified(mockNameSuggestionRepository)
    }

    @Test
    @DisplayName(
        """
        When handling a `ItemAddedToList` UI event
        Then expect item to be added to the list and a toast message to be displayed
    """
    )
    fun onItemAddedToList() = runTest {
        val name = "Milk"

        val shoppingList = createShoppingList()
        every { mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID) } returns flowOf(shoppingList)

        val viewModel = createViewModel(currentSearchTermValue = "Search term")
        viewModel.viewState.test {
            skipItems(1)

            viewModel.onUiEvent(UiEvent.ItemAddedToList(name))
            val expectedToastMessage = ToastMessage(textResourceId = R.string.toast_item_added, arguments = persistentListOf(name))
            val expectedViewState = ViewState(suggestions = persistentListOf(), toastMessage = expectedToastMessage)
            assertThat(awaitItem()).isDataClassEqualTo(expectedViewState)

            cancelAndConsumeRemainingEvents()
        }

        coVerify { mockShoppingItemRepository.create(eq(ShoppingItem(name = name, isChecked = false, list = shoppingList))) }
        confirmVerified(mockShoppingItemRepository, mockNameSuggestionRepository)

        assertThat(viewModel.searchTermState.value).isEmpty()
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
        viewModel.viewState.test {
            skipItems(1)

            viewModel.onUiEvent(UiEvent.ItemAddedToList(""))
            expectNoEvents()
        }

        coVerify(exactly = 0) { mockShoppingItemRepository.create(any()) }
        confirmVerified(mockShoppingItemRepository, mockNameSuggestionRepository)

        assertThat(viewModel.searchTermState.value).isEqualTo("Search term")
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
        coEvery { mockNameSuggestionRepository.findAllMatching(any()) } returns emptyList()

        val suggestion = NameSuggestion(name = "Milk")

        val viewModel = createViewModel()
        viewModel.viewState.test {
            skipItems(1)

            viewModel.onUiEvent(UiEvent.SuggestionDeleted(suggestion))
            val expectedToastMessage = ToastMessage(textResourceId = R.string.toast_suggestion_removed, arguments = persistentListOf("Milk"))
            val expectedViewState = ViewState(suggestions = persistentListOf(), toastMessage = expectedToastMessage)
            assertThat(awaitItem()).isDataClassEqualTo(expectedViewState)

            cancelAndConsumeRemainingEvents()
        }

        coVerify { mockNameSuggestionRepository.findAllMatching(any()) }
        coVerify { mockNameSuggestionRepository.delete(suggestion) }
        confirmVerified(mockNameSuggestionRepository)
    }

    @Test
    @DisplayName(
        """
        When handling a `ToastShown` UI event
        Then expect toast message to be cleared
    """
    )
    fun onToastShown() = runTest {
        every { mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID) } returns flowOf(createShoppingList())
        coEvery { mockNameSuggestionRepository.findAllMatching(any()) } returns emptyList()

        val suggestion = NameSuggestion(name = "Milk")

        val viewModel = createViewModel()
        viewModel.viewState.test {
            skipItems(1)

            viewModel.onUiEvent(UiEvent.SuggestionDeleted(suggestion))
            assertThat(awaitItem()).prop(ViewState::toastMessage).isNotNull()

            viewModel.onUiEvent(UiEvent.ToastShown)
            assertThat(awaitItem()).isDataClassEqualTo(ViewState(suggestions = persistentListOf(), toastMessage = null))

            cancelAndConsumeRemainingEvents()
        }
    }

    private fun TestScope.createViewModel(currentSearchTermValue: String = "") =
        AddItemsViewModel(
            shoppingListId = DEFAULT_SHOPPING_LIST_ID,
            nameSuggestionRepository = mockNameSuggestionRepository,
            shoppingItemRepository = mockShoppingItemRepository,
            shoppingListRepository = mockShoppingListRepository,
            coroutineScope = CoroutineScope(coroutineContext + SupervisorJob())
        ).apply { searchTermState.value = currentSearchTermValue }
}
