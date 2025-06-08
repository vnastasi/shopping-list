package md.vnastasi.shoppinglist.db.dao

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.isEmpty
import md.vnastasi.shoppinglist.db.ShoppingListDatabase
import md.vnastasi.shoppinglist.db.model.NameSuggestion
import md.vnastasi.shoppinglist.db.model.ShoppingItem
import md.vnastasi.shoppinglist.db.model.ShoppingList
import md.vnastasi.shoppinglist.db.support.runDatabaseTest
import org.junit.Test

class NameSuggestionDaoTest {

    @Test
    fun emptySearchTerm() = runDatabaseTest { db ->
        db.createTestSuggestions()

        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao()
        assertThat(shoppingItemNameSuggestionDao.findAll(""))
            .extracting { it.value }
            .containsExactly("Toothbrush", "Toothpaste", "Butter", "Nutmeg", "Pasta", "Eggs", "Bread")
    }

    @Test
    fun caseInsensitiveSearch() = runDatabaseTest { db ->
        db.createTestSuggestions()

        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao()
        assertThat(shoppingItemNameSuggestionDao.findAll("toOtH"))
            .extracting { it.value }
            .containsExactly("Toothbrush", "Toothpaste")
    }

    @Test
    fun noMatchingValues() = runDatabaseTest { db ->
        db.createTestSuggestions()

        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao()
        assertThat(shoppingItemNameSuggestionDao.findAll("qwerty"))
            .isEmpty()
    }

    @Test
    fun delete() = runDatabaseTest { db ->
        db.createTestSuggestions()

        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao()

        shoppingItemNameSuggestionDao.delete(NameSuggestion(5L, "Butter"))
        shoppingItemNameSuggestionDao.delete(NameSuggestion(1L, "Bread"))

        assertThat(shoppingItemNameSuggestionDao.findAll(""))
            .extracting { it.value }
            .containsExactly("Toothbrush", "Toothpaste", "Nutmeg", "Pasta", "Eggs")
    }

    private suspend fun ShoppingListDatabase.createTestSuggestions() {
        val shoppingListDao = shoppingListDao()
        val shoppingItemDao = shoppingItemDao()

        shoppingListDao.create(ShoppingList(id = 1L, name = "Test"))

        sequenceOf(
            ShoppingItem(id = 1L, name = "Bread", isChecked = false, listId = 1L),
            ShoppingItem(id = 2L, name = "Eggs", isChecked = false, listId = 1L),
            ShoppingItem(id = 3L, name = "Pasta", isChecked = false, listId = 1L),
            ShoppingItem(id = 4L, name = "Nutmeg", isChecked = false, listId = 1L),
            ShoppingItem(id = 5L, name = "Butter", isChecked = false, listId = 1L),
            ShoppingItem(id = 6L, name = "Toothpaste", isChecked = false, listId = 1L),
            ShoppingItem(id = 7L, name = "Toothbrush", isChecked = false, listId = 1L)
        ).forEach { shoppingItem ->
            shoppingItemDao.create(shoppingItem)
        }
    }
}
