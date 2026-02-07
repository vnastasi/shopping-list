package md.vnastasi.shoppinglist.db.dao

import app.cash.turbine.test
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsExactly
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEmpty
import assertk.assertions.isFailure
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
            assertThat(awaitItem()).containsExactly(createShoppingListDetailsView())

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
            assertThat(awaitItem()).containsExactly(createShoppingListDetailsView())

            val updatedShoppingList = shoppingList.copy(name = "Other")
            shoppingListDao.update(updatedShoppingList)
            assertThat(awaitItem()).containsExactly(createShoppingListDetailsView { name = "Other" })

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateMultipleShoppingLists() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        val shoppingList1 = createShoppingListEntity {
            id = 1L
            name = "L1"
            position = 0
        }
        val shoppingList2 = createShoppingListEntity {
            id = 2L
            name = "L2"
            position = 1
        }
        shoppingListDao.create(shoppingList1)
        shoppingListDao.create(shoppingList2)

        val expectedShoppingListDetails1 = createShoppingListDetailsView {
            id = 1L
            name = "L1"
            position = 0
        }
        val expectedShoppingListDetails2 = createShoppingListDetailsView {
            id = 2L
            name = "L2"
            position = 1
        }

        shoppingListDao.findAll().test {
            assertThat(awaitItem()).containsExactly(expectedShoppingListDetails1, expectedShoppingListDetails2)

            shoppingListDao.update(listOf(shoppingList1.copy(position = 1), shoppingList2.copy(position = 0)))
            assertThat(awaitItem()).containsExactly(expectedShoppingListDetails2.copy(position = 0), expectedShoppingListDetails1.copy(position = 1))

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteShoppingList() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.create(shoppingList)

        shoppingListDao.findAll().test {
            assertThat(awaitItem()).containsExactly(createShoppingListDetailsView())

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
            assertThat(awaitItem()).containsExactly(shoppingItem)

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
            assertFailure { awaitItem() }
            cancelAndIgnoreRemainingEvents()
        }
    }
}
