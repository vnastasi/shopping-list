package md.vnastasi.shoppinglist.db.dao

import app.cash.turbine.test
import assertk.all
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEmpty
import assertk.assertions.isFailure
import assertk.assertions.isNull
import md.vnastasi.shoppinglist.db.TestData.DEFAULT_SHOPPING_LIST_ID
import md.vnastasi.shoppinglist.db.TestData.DEFAULT_SHOPPING_LIST_NAME
import md.vnastasi.shoppinglist.db.TestData.createShoppingItemEntity
import md.vnastasi.shoppinglist.db.TestData.createShoppingListDetailsView
import md.vnastasi.shoppinglist.db.TestData.createShoppingListEntity
import md.vnastasi.shoppinglist.db.support.runDatabaseTest
import org.junit.Test

class ShoppingListDaoTest {

    @Test
    fun createShoppingList() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        shoppingListDao.findAll().test {
            assertThat(awaitItem()).isEmpty()

            val shoppingList = createShoppingListEntity()
            shoppingListDao.create(shoppingList)
            assertThat(awaitItem()).all { hasSize(1); contains(createShoppingListDetailsView()) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun createShoppingListWithExistingId() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.create(shoppingList)
        assertThat(runCatching { shoppingListDao.create(shoppingList) }).isFailure()
    }

    @Test
    fun updateShoppingList() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.create(shoppingList)

        shoppingListDao.findAll().test {
            assertThat(awaitItem()).all { hasSize(1); contains(createShoppingListDetailsView()) }

            val updatedShoppingList = shoppingList.copy(name = "Other")
            shoppingListDao.update(updatedShoppingList)
            assertThat(awaitItem()).all { hasSize(1); contains(createShoppingListDetailsView { name = "Other" }) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteShoppingList() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.create(shoppingList)

        shoppingListDao.findAll().test {
            assertThat(awaitItem()).all { hasSize(1); contains(createShoppingListDetailsView()) }

            shoppingListDao.delete(shoppingList)
            assertThat(awaitItem()).isEmpty()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun cascadeDeleteShoppingList() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.create(shoppingList)

        val shoppingItem = createShoppingItemEntity()
        shoppingItemDao.create(shoppingItem)

        shoppingItemDao.findAll(DEFAULT_SHOPPING_LIST_ID).test {
            assertThat(awaitItem()).all { hasSize(1); contains(shoppingItem) }

            shoppingListDao.delete(shoppingList)
            assertThat(awaitItem()).isEmpty()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getShoppingListDetails() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.create(shoppingList)

        shoppingItemDao.create(createShoppingItemEntity {
            id = 1L
            listId = DEFAULT_SHOPPING_LIST_ID
            name = "1"
            isChecked = true
        })
        shoppingItemDao.create(createShoppingItemEntity {
            id = 2L
            listId = DEFAULT_SHOPPING_LIST_ID
            name = "2"
            isChecked = true
        })
        shoppingItemDao.create(createShoppingItemEntity {
            id = 3L
            listId = DEFAULT_SHOPPING_LIST_ID
            name = "3"
            isChecked = false
        })

        val expectedDetails = createShoppingListDetailsView {
            id = DEFAULT_SHOPPING_LIST_ID
            name = DEFAULT_SHOPPING_LIST_NAME
            totalItems = 3L
            checkedItems = 2L
        }

        shoppingListDao.findAll().test {
            assertThat(awaitItem()).contains(expectedDetails)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getExistingShoppingListById() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.create(shoppingList)

        shoppingListDao.findById(DEFAULT_SHOPPING_LIST_ID).test {
            assertThat(awaitItem()).isDataClassEqualTo(shoppingList)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getNonExistingShoppingListById() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.create(shoppingList)

        shoppingListDao.findById(-1L).test {
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
