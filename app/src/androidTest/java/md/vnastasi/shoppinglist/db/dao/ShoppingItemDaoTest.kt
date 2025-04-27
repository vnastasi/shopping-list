package md.vnastasi.shoppinglist.db.dao

import app.cash.turbine.test
import assertk.all
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEmpty
import assertk.assertions.isFailure
import md.vnastasi.shoppinglist.db.TestData.DEFAULT_SHOPPING_LIST_ID
import md.vnastasi.shoppinglist.db.TestData.DEFAULT_SHOPPING_LIST_ITEM_ID
import md.vnastasi.shoppinglist.db.TestData.createShoppingItemEntity
import md.vnastasi.shoppinglist.db.TestData.createShoppingListEntity
import md.vnastasi.shoppinglist.db.support.runDatabaseTest
import org.junit.Test

class ShoppingItemDaoTest {

    @Test
    fun createShoppingItem() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        shoppingListDao.create(createShoppingListEntity())

        shoppingItemDao.findAll(DEFAULT_SHOPPING_LIST_ID).test {
            assertThat(awaitItem()).isEmpty()

            val shoppingItem = createShoppingItemEntity()
            shoppingItemDao.create(shoppingItem)
            assertThat(awaitItem()).all { hasSize(1); contains(shoppingItem) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun createShoppingItemWithSameId() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        shoppingListDao.create(createShoppingListEntity())

        val shoppingItem = createShoppingItemEntity()
        shoppingItemDao.create(shoppingItem)
        assertThat(runCatching { shoppingItemDao.create(shoppingItem) }).isFailure()
    }

    @Test
    fun createShoppingItemWithNonExistingListId() = runDatabaseTest { db ->
        val shoppingItemDao = db.shoppingItemDao()

        val shoppingItem = createShoppingItemEntity { listId = 1L }
        assertThat(runCatching { shoppingItemDao.create(shoppingItem) }).isFailure()
    }

    @Test
    fun updateShoppingItem() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        shoppingListDao.create(createShoppingListEntity())

        shoppingItemDao.findAll(DEFAULT_SHOPPING_LIST_ID).test {
            assertThat(awaitItem()).isEmpty()

            val shoppingItem = createShoppingItemEntity()
            shoppingItemDao.create(shoppingItem)
            assertThat(awaitItem()).all { hasSize(1); contains(shoppingItem) }

            val updatedShoppingItem = shoppingItem.copy(isChecked = true)
            shoppingItemDao.update(updatedShoppingItem)
            assertThat(awaitItem()).all { hasSize(1); contains(updatedShoppingItem) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteShoppingItem() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        shoppingListDao.create(createShoppingListEntity())

        val shoppingItem = createShoppingItemEntity()
        shoppingItemDao.create(shoppingItem)

        shoppingItemDao.findAll(DEFAULT_SHOPPING_LIST_ID).test {
            assertThat(awaitItem()).all { hasSize(1); contains(shoppingItem) }

            shoppingItemDao.delete(shoppingItem)
            assertThat(awaitItem()).isEmpty()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getExistingShoppingItemById() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        shoppingListDao.create(createShoppingListEntity())

        val shoppingItem = createShoppingItemEntity()
        shoppingItemDao.create(shoppingItem)

        shoppingItemDao.findById(DEFAULT_SHOPPING_LIST_ITEM_ID).test {
            assertThat(awaitItem()).isDataClassEqualTo(shoppingItem)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getNonExistingShoppingItemById() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        shoppingListDao.create(createShoppingListEntity())

        val shoppingItem = createShoppingItemEntity()
        shoppingItemDao.create(shoppingItem)

        shoppingItemDao.findById(-1L).test {
            assertFailure { awaitItem() }
            cancelAndIgnoreRemainingEvents()
        }
    }
}
