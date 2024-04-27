package md.vnastasi.shoppinglist.db.dao

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.isEmpty
import md.vnastasi.shoppinglist.db.model.NameSuggestion
import md.vnastasi.shoppinglist.db.support.runDatabaseTest
import org.junit.Test

class NameSuggestionDaoTest {

    @Test
    fun emptySearchTerm() = runDatabaseTest { db ->
        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao().also { it.createTestSuggestions() }
        assertThat(shoppingItemNameSuggestionDao.findAll(""))
            .extracting { it.value }
            .containsExactly("Toothbrush", "Toothpaste", "Butter", "Nutmeg", "Pasta", "Eggs", "Bread")
    }

    @Test
    fun caseInsensitiveSearch() = runDatabaseTest { db ->
        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao().also { it.createTestSuggestions() }
        assertThat(shoppingItemNameSuggestionDao.findAll("toOtH"))
            .extracting { it.value }
            .containsExactly("Toothbrush", "Toothpaste")
    }

    @Test
    fun noMatchingValues() = runDatabaseTest { db ->
        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao().also { it.createTestSuggestions() }
        assertThat(shoppingItemNameSuggestionDao.findAll("qwerty"))
            .isEmpty()
    }

    @Test
    fun delete() = runDatabaseTest { db ->
        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao().also { it.createTestSuggestions() }

        shoppingItemNameSuggestionDao.delete(NameSuggestion(5L, "Butter"))
        shoppingItemNameSuggestionDao.delete(NameSuggestion(1L, "Bread"))

        assertThat(shoppingItemNameSuggestionDao.findAll(""))
            .extracting { it.value }
            .containsExactly("Toothbrush", "Toothpaste", "Nutmeg", "Pasta", "Eggs")
    }

    private suspend fun NameSuggestionDao.createTestSuggestions() {
        create(NameSuggestion(1L, "Bread"))
        create(NameSuggestion(2L, "Eggs"))
        create(NameSuggestion(3L, "Pasta"))
        create(NameSuggestion(4L, "Nutmeg"))
        create(NameSuggestion(5L, "Butter"))
        create(NameSuggestion(6L, "Toothpaste"))
        create(NameSuggestion(7L, "Toothbrush"))
    }
}
