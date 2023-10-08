package md.vnastasi.shoppinglist.db.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import assertk.all
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEmpty
import assertk.assertions.isFailure
import assertk.assertions.isNull
import md.vnastasi.shoppinglist.support.db.runDatabaseTest
import md.vnastasi.shoppinglist.support.testdata.TestData.DEFAULT_SHOPPING_LIST_ID
import md.vnastasi.shoppinglist.support.testdata.TestData.DEFAULT_SHOPPING_LIST_ITEM_ID
import md.vnastasi.shoppinglist.support.testdata.TestData.createShoppingItemEntity
import md.vnastasi.shoppinglist.support.testdata.TestData.createShoppingListEntity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingItemDaoTest {

    @Test
    fun createShoppingItem() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        shoppingListDao.addShoppingList(createShoppingListEntity())

        shoppingItemDao.getAllShoppingItems(DEFAULT_SHOPPING_LIST_ID).test {
            assertThat(awaitItem()).isEmpty()

            val shoppingItem = createShoppingItemEntity()
            shoppingItemDao.addShoppingItem(shoppingItem)
            assertThat(awaitItem()).all { hasSize(1); contains(shoppingItem) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun createShoppingItemWithSameId() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        shoppingListDao.addShoppingList(createShoppingListEntity())

        val shoppingItem = createShoppingItemEntity()
        shoppingItemDao.addShoppingItem(shoppingItem)
        assertThat(runCatching { shoppingItemDao.addShoppingItem(shoppingItem) }).isFailure()
    }

    @Test
    fun createShoppingItemWithNonExistingListId() = runDatabaseTest { db ->
        val shoppingItemDao = db.shoppingItemDao()

        val shoppingItem = createShoppingItemEntity { listId = 1L }
        assertThat(runCatching { shoppingItemDao.addShoppingItem(shoppingItem) }).isFailure()
    }

    @Test
    fun updateShoppingItem() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        shoppingListDao.addShoppingList(createShoppingListEntity())

        shoppingItemDao.getAllShoppingItems(DEFAULT_SHOPPING_LIST_ID).test {
            assertThat(awaitItem()).isEmpty()

            val shoppingItem = createShoppingItemEntity()
            shoppingItemDao.addShoppingItem(shoppingItem)
            assertThat(awaitItem()).all { hasSize(1); contains(shoppingItem) }

            val updatedShoppingItem = shoppingItem.copy(isChecked = true)
            shoppingItemDao.updateShoppingItem(updatedShoppingItem)
            assertThat(awaitItem()).all { hasSize(1); contains(updatedShoppingItem) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteShoppingItem() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        shoppingListDao.addShoppingList(createShoppingListEntity())

        val shoppingItem = createShoppingItemEntity()
        shoppingItemDao.addShoppingItem(shoppingItem)

        shoppingItemDao.getAllShoppingItems(DEFAULT_SHOPPING_LIST_ID).test {
            assertThat(awaitItem()).all { hasSize(1); contains(shoppingItem) }

            shoppingItemDao.deleteShoppingItem(shoppingItem)
            assertThat(awaitItem()).isEmpty()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getExistingShoppingItemById() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        shoppingListDao.addShoppingList(createShoppingListEntity())

        val shoppingItem = createShoppingItemEntity()
        shoppingItemDao.addShoppingItem(shoppingItem)

        assertThat(shoppingItemDao.getShoppingItemById(DEFAULT_SHOPPING_LIST_ITEM_ID)).isDataClassEqualTo(shoppingItem)
    }

    @Test
    fun getNonExistingShoppingItemById() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        shoppingListDao.addShoppingList(createShoppingListEntity())

        val shoppingItem = createShoppingItemEntity()
        shoppingItemDao.addShoppingItem(shoppingItem)

        assertThat(shoppingItemDao.getShoppingItemById(-1L)).isNull()
    }
}
