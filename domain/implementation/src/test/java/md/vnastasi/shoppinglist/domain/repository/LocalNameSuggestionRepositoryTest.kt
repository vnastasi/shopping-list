package md.vnastasi.shoppinglist.domain.repository

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.db.dao.NameSuggestionDao
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import md.vnastasi.shoppinglist.db.model.NameSuggestion as NameSuggestionEntity

internal class LocalNameSuggestionRepositoryTest {

    private val mockNameSuggestionDao = mockk<NameSuggestionDao>(relaxUnitFun = true)

    private val repository = LocalNameSuggestionRepository(mockNameSuggestionDao)

    @Test
    @DisplayName("Given empty search term Then expect no suggestions")
    fun emptySearchTermReturnsNoSuggestions() = runTest {
        repository.findAllMatching("").test {
            assertThat(awaitItem()).isEmpty()
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 0) { mockNameSuggestionDao.findAll(any()) }
    }

    @Test
    @DisplayName("Given search term of length 1 Then expect suggestions of search item")
    fun searchTermLength1ReturnsSuggestionWithSearchTerm() = runTest {
        val searchTerm = "a"

        repository.findAllMatching(searchTerm).test {
            assertThat(awaitItem()).containsExactly(NameSuggestion(-1L, searchTerm))
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 0) { mockNameSuggestionDao.findAll(any()) }
        confirmVerified(mockNameSuggestionDao)
    }

    @Test
    @DisplayName("Given search term of length 2 Then expect suggestions of search item")
    fun searchTermLength2ReturnsSuggestionWithSearchTerm() = runTest {
        val searchTerm = "ab"

        repository.findAllMatching(searchTerm).test {
            assertThat(awaitItem()).containsExactly(NameSuggestion(-1L, searchTerm))
            cancelAndConsumeRemainingEvents()
        }

        coVerify(exactly = 0) { mockNameSuggestionDao.findAll(any()) }
        confirmVerified(mockNameSuggestionDao)
    }

    @Test
    @DisplayName("Given search term of length 3 and no values from database Then expect suggestions of search item")
    fun searchTermLength3AndNoDatabaseValuesReturnsSuggestionWithSearchTerm() = runTest {
        val searchTerm = "abc"
        coEvery { mockNameSuggestionDao.findAll(searchTerm) } returns flowOf(emptyList())

        repository.findAllMatching(searchTerm).test {
            assertThat(awaitItem()).containsExactly(NameSuggestion(-1L, searchTerm))
            cancelAndConsumeRemainingEvents()
        }

        coVerify { mockNameSuggestionDao.findAll(searchTerm) }
        confirmVerified(mockNameSuggestionDao)
    }

    @Test
    @DisplayName("Given search term of length 3 and existing values from database Then expect suggestions of search item plus values from database")
    fun searchTermLength3AndExistingDatabaseValuesReturnsSuggestionWithSearchTermAndDatabaseValues() = runTest {
        val searchTerm = "abc"
        coEvery { mockNameSuggestionDao.findAll(searchTerm) } returns flowOf(
            listOf(
                NameSuggestionEntity(1L, "def"),
                NameSuggestionEntity(2L, "ghi")
            )
        )

        repository.findAllMatching(searchTerm).test {
            assertThat(awaitItem()).containsExactly(
                NameSuggestion(-1L, searchTerm),
                NameSuggestion(1L, "def"),
                NameSuggestion(2L, "ghi")
            )
            cancelAndConsumeRemainingEvents()
        }

        coVerify { mockNameSuggestionDao.findAll(searchTerm) }
        confirmVerified(mockNameSuggestionDao)
    }

    @Test
    @DisplayName("Given a suggestion name When deleting a suggestion Then expect suggestion to be deleted")
    fun delete() = runTest {
        repository.delete(NameSuggestion(id = 23L, name = "Sample"))

        coVerify { mockNameSuggestionDao.delete(NameSuggestionEntity(id = 23L, value = "Sample")) }
        confirmVerified(mockNameSuggestionDao)
    }
}
