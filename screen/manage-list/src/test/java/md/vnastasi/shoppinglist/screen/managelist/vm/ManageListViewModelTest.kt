package md.vnastasi.shoppinglist.screen.managelist.vm

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEqualTo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingList
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import md.vnastasi.shoppinglist.screen.managelist.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.managelist.model.TextValidationError
import md.vnastasi.shoppinglist.screen.managelist.model.UiEvent
import md.vnastasi.shoppinglist.screen.managelist.model.ViewState
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ManageListViewModelTest {

    private val mockShoppingListRepository = mockk<ShoppingListRepository>(relaxUnitFun = true)

    @Test
    @DisplayName(
        """
            Given no shopping list ID
            When bottom sheet is initialized
            Then expect text field to be empty, no validation errors and save to be disabled
        """
    )
    fun initialViewState() = runTest {
        val viewModel = createViewModel()
        viewModel.viewState.test {
            val expectedViewState = ViewState(validationError = TextValidationError.NONE, isSaveEnabled = false, navigationTarget = null)
            assertThat(awaitItem()).isDataClassEqualTo(expectedViewState)
        }

        assertThat(viewModel.listNameTextFieldState.text).isEqualTo("")
    }

    @Test
    @DisplayName(
        """
            When name changed to ''
            Then expect text field to be empty, 'EMPTY' validation error and save to be disabled
        """
    )
    fun validationErrorOnEmpty() = runTest {
        val viewModel = createViewModel()
        viewModel.viewState.test {
            skipItems(1)

            viewModel.dispatch(UiEvent.OnNameChanged(""))

            val expectedViewState = ViewState(validationError = TextValidationError.EMPTY, isSaveEnabled = false, navigationTarget = null)
            assertThat(awaitItem()).isDataClassEqualTo(expectedViewState)
        }
    }

    @Test
    @DisplayName(
        """
            When name changed to ' '
            Then expect text field to be  ' ', 'BLANK' validation error and save to be disabled
        """
    )
    fun validationErrorOnBlank() = runTest {
        val viewModel = createViewModel()
        viewModel.viewState.test {
            skipItems(1)

            viewModel.dispatch(UiEvent.OnNameChanged(" "))

            val expectedViewState = ViewState(validationError = TextValidationError.BLANK, isSaveEnabled = false, navigationTarget = null)
            assertThat(awaitItem()).isDataClassEqualTo(expectedViewState)
        }
    }

    @Test
    @DisplayName(
        """
            When name changed to 'a'
            Then expect text field to be 'a', no validation error and save to be enabled
        """
    )
    fun noValidationErrors() = runTest {
        val viewModel = createViewModel()
        viewModel.viewState.test {
            skipItems(1)

            viewModel.dispatch(UiEvent.OnNameChanged("a"))

            val expectedViewState = ViewState(validationError = TextValidationError.NONE, isSaveEnabled = true, navigationTarget = null)
            assertThat(awaitItem()).isDataClassEqualTo(expectedViewState)
        }
    }

    @Test
    @DisplayName(
        """
            Given no shopping list ID
            When saving
            Then expect new shopping list to be created with supplied name and navigation target set to `CloseSheet`
        """
    )
    fun createNewList() = runTest {
        val shoppingListSlot = slot<ShoppingList>()
        coEvery { mockShoppingListRepository.create(capture(shoppingListSlot)) } returns Unit

        val viewModel = createViewModel()
        viewModel.viewState.test {
            viewModel.dispatch(UiEvent.OnNameSaved("list"))
            advanceUntilIdle()

            val expectedViewState = ViewState(validationError = TextValidationError.NONE, isSaveEnabled = false, navigationTarget = NavigationTarget.CloseSheet)
            assertThat(expectMostRecentItem()).isDataClassEqualTo(expectedViewState)
        }

        assertThat(shoppingListSlot.captured).isDataClassEqualTo(ShoppingList(id = 0L, name = "list"))

        coVerify(exactly = 0) { mockShoppingListRepository.findById(any()) }
    }

    @Test
    @DisplayName(
        """
            Given ID of existing shopping list
            When saving
            Then expect name of existing shopping list to be updated and navigation target set to `CloseSheet`
        """
    )
    fun updateExistingList() = runTest {
        val shoppingListId = 345L
        val shoppingList = createShoppingList {
            id = shoppingListId
        }
        every { mockShoppingListRepository.findById(shoppingListId) } returns flowOf(shoppingList)

        val shoppingListSlot = slot<ShoppingList>()
        coEvery { mockShoppingListRepository.update(capture(shoppingListSlot)) } returns Unit

        val viewModel = createViewModel(shoppingListId)
        viewModel.viewState.test {
            viewModel.dispatch(UiEvent.OnNameSaved("updated"))
            advanceUntilIdle()

            val expectedViewState = ViewState(validationError = TextValidationError.NONE, isSaveEnabled = false, navigationTarget = NavigationTarget.CloseSheet)
            assertThat(expectMostRecentItem()).isDataClassEqualTo(expectedViewState)
        }

        assertThat(shoppingListSlot.captured).isDataClassEqualTo(ShoppingList(id = shoppingListId, name = "updated"))
    }

    @Test
    @DisplayName(
        """
            When handling a `OnNavigationConsumed` UI event
            Then expect navigation target to be set to `null`
        """
    )
    fun onNavigationConsumed() = runTest {
        val viewModel = createViewModel()

        viewModel.viewState.test {
            // Trigger an event to set a navigation target
            viewModel.dispatch(UiEvent.OnNameSaved("test"))
            advanceUntilIdle()
            assertThat(expectMostRecentItem()).isDataClassEqualTo(ViewState(validationError = TextValidationError.NONE, isSaveEnabled = false, navigationTarget = NavigationTarget.CloseSheet))

            // Consume it
            viewModel.dispatch(UiEvent.OnNavigationConsumed)
            advanceUntilIdle()

            val expectedViewState = ViewState(validationError = TextValidationError.NONE, isSaveEnabled = false, navigationTarget = null)
            assertThat(expectMostRecentItem()).isDataClassEqualTo(expectedViewState)
        }
    }

    context(scope: TestScope)
    private fun createViewModel(shoppingListId: Long? = null) = ManageListViewModel(
        shoppingListId = shoppingListId,
        repository = mockShoppingListRepository,
        coroutineScope = CoroutineScope(scope.coroutineContext + SupervisorJob() + StandardTestDispatcher(scope.testScheduler))
    )
}
