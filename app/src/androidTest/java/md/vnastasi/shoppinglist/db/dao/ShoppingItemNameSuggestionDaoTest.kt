package md.vnastasi.shoppinglist.db.dao

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import md.vnastasi.shoppinglist.db.model.ShoppingItemNameSuggestion
import md.vnastasi.shoppinglist.support.db.runDatabaseTest
import org.junit.Test

class ShoppingItemNameSuggestionDaoTest {

    @Test
    fun emptySearchTerm() = runDatabaseTest { db ->
        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao().also { it.createTestSuggestions() }

        shoppingItemNameSuggestionDao.findAll("").test {
            assertThat(awaitItem()).containsExactly("Toothbrush", "Toothpaste", "Butter", "Nutmeg", "Pasta", "Eggs", "Bread")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun caseInsensitiveSearch() = runDatabaseTest { db ->
        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao().also { it.createTestSuggestions() }

        shoppingItemNameSuggestionDao.findAll("toOtH").test {
            assertThat(awaitItem()).containsExactly("Toothbrush", "Toothpaste")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun noMatchingValues() = runDatabaseTest { db ->
        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao().also { it.createTestSuggestions() }

        shoppingItemNameSuggestionDao.findAll("qwerty").test {
            assertThat(awaitItem()).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun delete() = runDatabaseTest { db ->
        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao().also { it.createTestSuggestions() }

        shoppingItemNameSuggestionDao.delete(ShoppingItemNameSuggestion(5L, "Butter"))
        shoppingItemNameSuggestionDao.delete(ShoppingItemNameSuggestion(1L, "Bread"))

        shoppingItemNameSuggestionDao.findAll("").test {
            assertThat(awaitItem()).containsExactly("Toothbrush", "Toothpaste", "Nutmeg", "Pasta", "Eggs")
            cancelAndIgnoreRemainingEvents()
        }
    }

    private suspend fun ShoppingItemNameSuggestionDao.createTestSuggestions() {
        create(ShoppingItemNameSuggestion(1L, "Bread"))
        create(ShoppingItemNameSuggestion(2L, "Eggs"))
        create(ShoppingItemNameSuggestion(3L, "Pasta"))
        create(ShoppingItemNameSuggestion(4L, "Nutmeg"))
        create(ShoppingItemNameSuggestion(5L, "Butter"))
        create(ShoppingItemNameSuggestion(6L, "Toothpaste"))
        create(ShoppingItemNameSuggestion(7L, "Toothbrush"))
    }
}
