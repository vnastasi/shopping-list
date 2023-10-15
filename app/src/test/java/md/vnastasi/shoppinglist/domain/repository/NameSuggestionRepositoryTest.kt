package md.vnastasi.shoppinglist.domain.repository

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.db.dao.NameSuggestionDao
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class NameSuggestionRepositoryTest {

    private val mockNameSuggestionDao = mock<NameSuggestionDao>()

    private val repository = NameSuggestionRepository(mockNameSuggestionDao)

    @Test
    @DisplayName("Given empty search term The expect no suggestions")
    fun emptySearchTermReturnsNoSuggestions() = runTest {
        repository.findAllMatching("").test {
            assertThat(awaitItem()).isEmpty()
            awaitComplete()
        }

        verify(mockNameSuggestionDao, never()).findAll(any())
    }

    @Test
    @DisplayName("Given search term of length 1 The expect suggestions of search item")
    fun searchTermLength1ReturnsSuggestionWithSearchTerm() = runTest {
        val searchTerm = "a"
        repository.findAllMatching(searchTerm).test {
            assertThat(awaitItem()).containsExactly(NameSuggestion(-1L, searchTerm))
            awaitComplete()
        }

        verify(mockNameSuggestionDao, never()).findAll(any())
    }

    @Test
    @DisplayName("Given search term of length 2 The expect suggestions of search item")
    fun searchTermLength2ReturnsSuggestionWithSearchTerm() = runTest {
        val searchTerm = "ab"
        repository.findAllMatching(searchTerm).test {
            assertThat(awaitItem()).containsExactly(NameSuggestion(-1L, searchTerm))
            awaitComplete()
        }

        verify(mockNameSuggestionDao, never()).findAll(any())
    }

    @Test
    @DisplayName("Given search term of length 3 and no values from database The expect suggestions of search item")
    fun searchTermLength3AndNoDatabaseValuesReturnsSuggestionWithSearchTerm() = runTest {
        val searchTerm = "abc"
        whenever(mockNameSuggestionDao.findAll(searchTerm)).doReturn(flowOf(emptyList()))

        repository.findAllMatching(searchTerm).test {
            assertThat(awaitItem()).containsExactly(NameSuggestion(-1L, searchTerm))
            awaitComplete()
        }

        verify(mockNameSuggestionDao).findAll(searchTerm)
    }

    @Test
    @DisplayName("Given search term of length 3 and existing values from database The expect suggestions of search item plus values from database")
    fun searchTermLength3AndExistingDatabaseValuesReturnsSuggestionWithSearchTermAndDatabaseValues() = runTest {
        val searchTerm = "abc"
        whenever(mockNameSuggestionDao.findAll(searchTerm)).doReturn(flowOf(listOf(md.vnastasi.shoppinglist.db.model.NameSuggestion(1L, "def"), md.vnastasi.shoppinglist.db.model.NameSuggestion(2L, "ghi"))))

        repository.findAllMatching(searchTerm).test {
            assertThat(awaitItem()).containsExactly(NameSuggestion(-1L, searchTerm), NameSuggestion(1L, "def"), NameSuggestion(2L, "ghi"))
            awaitComplete()
        }

        verify(mockNameSuggestionDao).findAll(searchTerm)
    }
}
