package md.vnastasi.shoppinglist.screen.additems

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.domain.TestData.DEFAULT_SHOPPING_LIST_ID
import md.vnastasi.shoppinglist.domain.TestData.createShoppingList
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.repository.NameSuggestionRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.additems.model.UiEvent
import md.vnastasi.shoppinglist.screen.additems.model.ViewState
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModel
import md.vnastasi.shoppinglist.support.async.TestDispatchersProvider
import md.vnastasi.shoppinglist.support.ui.toast.ToastMessage
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class AddItemsViewModelTest {

    private val mockNameSuggestionRepository = mock<NameSuggestionRepository>()
    private val mockShoppingListRepository = mock<ShoppingListRepository>()
    private val mockShoppingItemRepository = mock<ShoppingItemRepository>()

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

        whenever(mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID)).doReturn(flowOf(createShoppingList()))
        whenever(mockNameSuggestionRepository.findAllMatching(searchTerm)).doReturn(flowOf(listOf(suggestion)))

        val viewModel = createViewModel()
        viewModel.screenState.test {
            skipItems(1)

            viewModel.onUiEvent(UiEvent.SearchTermChanged(searchTerm))
            assertThat(awaitItem()).isDataClassEqualTo(ViewState(searchTerm = searchTerm, suggestions = persistentListOf(suggestion), toastMessage = null))

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    @DisplayName(
        """
        When handling a `ItemAddedToList` UI event
        Then expect item to be added to the list and name to be added to the suggestions and a toast message to be displayed
    """
    )
    fun onItemAddedToList() = runTest {
        val name = "Milk"

        val shoppingList = createShoppingList()
        whenever(mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID)).doReturn(flowOf(shoppingList))

        val viewModel = createViewModel()
        viewModel.screenState.test {
            skipItems(1)

            viewModel.onUiEvent(UiEvent.ItemAddedToList(name))
            val expectedToastMessage = ToastMessage(textResourceId = R.string.toast_item_added, arguments = persistentListOf(name))
            val expectedViewState = ViewState(searchTerm = "", suggestions = persistentListOf(), toastMessage = expectedToastMessage)
            assertThat(awaitItem()).isDataClassEqualTo(expectedViewState)

            cancelAndConsumeRemainingEvents()
        }

        verify(mockShoppingItemRepository).create(eq(ShoppingItem(name = name, isChecked = false, list = shoppingList)))
        verify(mockNameSuggestionRepository).create(name)
    }

    @Test
    @DisplayName(
        """
        When handling a `ItemAddedToList` UI event with an empty string
        Then expect no further action
    """
    )
    fun onEmptyItemAddedToList() = runTest {
        val name = ""

        val shoppingList = createShoppingList()
        whenever(mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID)).doReturn(flowOf(shoppingList))

        val viewModel = createViewModel()
        viewModel.screenState.test {
            skipItems(1)

            viewModel.onUiEvent(UiEvent.ItemAddedToList(name))
            expectNoEvents()
        }

        verify(mockShoppingItemRepository, never()).create(any())
        verify(mockNameSuggestionRepository, never()).create(any())
    }

    @Test
    @DisplayName(
        """
        When handling a `SuggestionDeleted` UI event
        Then expect suggestion to be deleted and a toast message to be displayed
    """
    )
    fun onSuggestionDeleted() = runTest {
        whenever(mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID)).doReturn(flowOf(createShoppingList()))

        val suggestion = NameSuggestion(name = "Milk")

        val viewModel = createViewModel()
        viewModel.screenState.test {
            skipItems(1)

            viewModel.onUiEvent(UiEvent.SuggestionDeleted(suggestion))
            val expectedToastMessage = ToastMessage(textResourceId = R.string.toast_suggestion_removed, arguments = persistentListOf("Milk"))
            val expectedViewState = ViewState(searchTerm = "", suggestions = persistentListOf(), toastMessage = expectedToastMessage)
            assertThat(awaitItem()).isDataClassEqualTo(expectedViewState)

            cancelAndConsumeRemainingEvents()
        }

        verify(mockNameSuggestionRepository).delete(suggestion)
    }

    @Test
    @DisplayName(
        """
        When handling a `ToastShown` UI event
        Then expect toast message to be cleared
    """
    )
    fun onToastShown() = runTest {
        whenever(mockShoppingListRepository.findById(DEFAULT_SHOPPING_LIST_ID)).doReturn(flowOf(createShoppingList()))

        val suggestion = NameSuggestion(name = "Milk")

        val viewModel = createViewModel()
        viewModel.screenState.test {
            skipItems(1)

            viewModel.onUiEvent(UiEvent.SuggestionDeleted(suggestion))
            assertThat(awaitItem()).prop(ViewState::toastMessage).isNotNull()

            viewModel.onUiEvent(UiEvent.ToastShown)
            assertThat(awaitItem()).isDataClassEqualTo(ViewState(searchTerm = "", suggestions = persistentListOf(), toastMessage = null))

            cancelAndConsumeRemainingEvents()
        }
    }

    context(TestScope)
    private fun createViewModel() = AddItemsViewModel(
        savedStateHandle = SavedStateHandle(mapOf(AddItemsViewModel.ARG_KEY_SHOPPING_LIST_ID to DEFAULT_SHOPPING_LIST_ID)),
        nameSuggestionRepository = mockNameSuggestionRepository,
        shoppingItemRepository = mockShoppingItemRepository,
        shoppingListRepository = mockShoppingListRepository,
        dispatchersProvider = TestDispatchersProvider(StandardTestDispatcher(testScheduler))
    )
}
