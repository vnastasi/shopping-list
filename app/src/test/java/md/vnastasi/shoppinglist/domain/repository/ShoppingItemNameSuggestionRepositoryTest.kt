package md.vnastasi.shoppinglist.domain.repository

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.db.dao.ShoppingItemNameSuggestionDao
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ShoppingItemNameSuggestionRepositoryTest {

    private val mockShoppingItemNameSuggestionDao = mock<ShoppingItemNameSuggestionDao>()

    private val repository = ShoppingItemNameSuggestionRepository(mockShoppingItemNameSuggestionDao)

    @Test
    @DisplayName("Given empty search term The expect no suggestions")
    fun emptySearchTermReturnsNoSuggestions() = runTest {
        repository.findAllMatching("").test {
            assertThat(awaitItem()).isEmpty()
            awaitComplete()
        }

        verify(mockShoppingItemNameSuggestionDao, never()).findAll(any())
    }

    @Test
    @DisplayName("Given search term of length 1 The expect suggestions of search item")
    fun searchTermLength1ReturnsSuggestionWithSearchTerm() = runTest {
        val searchTerm = "a"
        repository.findAllMatching(searchTerm).test {
            assertThat(awaitItem()).containsExactly(searchTerm)
            awaitComplete()
        }

        verify(mockShoppingItemNameSuggestionDao, never()).findAll(any())
    }

    @Test
    @DisplayName("Given search term of length 2 The expect suggestions of search item")
    fun searchTermLength2ReturnsSuggestionWithSearchTerm() = runTest {
        val searchTerm = "ab"
        repository.findAllMatching(searchTerm).test {
            assertThat(awaitItem()).containsExactly(searchTerm)
            awaitComplete()
        }

        verify(mockShoppingItemNameSuggestionDao, never()).findAll(any())
    }

    @Test
    @DisplayName("Given search term of length 3 and no values from database The expect suggestions of search item")
    fun searchTermLength3AndNoDatabaseValuesReturnsSuggestionWithSearchTerm() = runTest {
        val searchTerm = "abc"
        whenever(mockShoppingItemNameSuggestionDao.findAll(searchTerm)).doReturn(flowOf(emptyList()))

        repository.findAllMatching(searchTerm).test {
            assertThat(awaitItem()).containsExactly(searchTerm)
            awaitComplete()
        }

        verify(mockShoppingItemNameSuggestionDao).findAll(searchTerm)
    }

    @Test
    @DisplayName("Given search term of length 3 and existing values from database The expect suggestions of search item plus values from database")
    fun searchTermLength3AndExistingDatabaseValuesReturnsSuggestionWithSearchTermAndDatabaseValues() = runTest {
        val searchTerm = "abc"
        whenever(mockShoppingItemNameSuggestionDao.findAll(searchTerm)).doReturn(flowOf(listOf("def", "ghi")))

        repository.findAllMatching(searchTerm).test {
            assertThat(awaitItem()).containsExactly(searchTerm, "def", "ghi")
            awaitComplete()
        }

        verify(mockShoppingItemNameSuggestionDao).findAll(searchTerm)
    }
}