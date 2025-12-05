package md.vnastasi.shoppinglist.db.dao

import app.cash.turbine.test
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
        shoppingItemNameSuggestionDao.findAll("").test {
            assertThat(expectMostRecentItem())
                .extracting(NameSuggestion::value)
                .containsExactly("Toothbrush", "Toothpaste", "Butter", "Nutmeg", "Pasta", "Eggs", "Bread")
        }
    }

    @Test
    fun caseInsensitiveSearch() = runDatabaseTest { db ->
        db.createTestSuggestions()

        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao()
        shoppingItemNameSuggestionDao.findAll("toOtH").test {
            assertThat(expectMostRecentItem())
                .extracting(NameSuggestion::value)
                .containsExactly("Toothbrush", "Toothpaste")
        }
    }

    @Test
    fun noMatchingValues() = runDatabaseTest { db ->
        db.createTestSuggestions()

        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao()
        shoppingItemNameSuggestionDao.findAll("qwerty").test {
            assertThat(expectMostRecentItem()).isEmpty()
        }
    }

    @Test
    fun delete() = runDatabaseTest { db ->
        db.createTestSuggestions()

        val shoppingItemNameSuggestionDao = db.shoppingItemNameSuggestionDao()

        shoppingItemNameSuggestionDao.delete(NameSuggestion(5L, "Butter"))
        shoppingItemNameSuggestionDao.delete(NameSuggestion(1L, "Bread"))

        shoppingItemNameSuggestionDao.findAll("").test {
            assertThat(expectMostRecentItem())
                .extracting(NameSuggestion::value)
                .containsExactly("Toothbrush", "Toothpaste", "Nutmeg", "Pasta", "Eggs")
        }
    }

    private suspend fun ShoppingListDatabase.createTestSuggestions() {
        val shoppingListDao = shoppingListDao()
        val shoppingItemDao = shoppingItemDao()

        val shoppingListId = shoppingListDao.create(ShoppingList(name = "Test"))

        sequenceOf(
            ShoppingItem(name = "Bread", isChecked = false, listId = shoppingListId),
            ShoppingItem(name = "Eggs", isChecked = false, listId = shoppingListId),
            ShoppingItem(name = "Pasta", isChecked = false, listId = shoppingListId),
            ShoppingItem(name = "Nutmeg", isChecked = false, listId = shoppingListId),
            ShoppingItem(name = "Butter", isChecked = false, listId = shoppingListId),
            ShoppingItem(name = "Toothpaste", isChecked = false, listId = shoppingListId),
            ShoppingItem(name = "Toothbrush", isChecked = false, listId = shoppingListId)
        ).forEach { shoppingItem ->
            shoppingItemDao.create(shoppingItem)
        }
    }
}
