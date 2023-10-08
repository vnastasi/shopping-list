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
import md.vnastasi.shoppinglist.support.testdata.TestData.createShoppingItemEntity
import md.vnastasi.shoppinglist.support.testdata.TestData.createShoppingListEntity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingListDaoTest {

    @Test
    fun createShoppingList() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        shoppingListDao.getShoppingLists().test {
            assertThat(awaitItem()).isEmpty()

            val shoppingList = createShoppingListEntity()
            shoppingListDao.addShoppingList(shoppingList)
            assertThat(awaitItem()).all { hasSize(1); contains(shoppingList) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun createShoppingListWithExistingId() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.addShoppingList(shoppingList)
        assertThat(runCatching { shoppingListDao.addShoppingList(shoppingList) }).isFailure()
    }

    @Test
    fun updateShoppingList() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.addShoppingList(shoppingList)

        shoppingListDao.getShoppingLists().test {
            assertThat(awaitItem()).all { hasSize(1); contains(shoppingList) }

            val updatedShoppingList = shoppingList.copy(name = "Other")
            shoppingListDao.updateShoppingList(updatedShoppingList)
            assertThat(awaitItem()).all { hasSize(1); contains(updatedShoppingList) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteShoppingList() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.addShoppingList(shoppingList)

        shoppingListDao.getShoppingLists().test {
            assertThat(awaitItem()).all { hasSize(1); contains(shoppingList) }

            shoppingListDao.deleteShoppingList(shoppingList)
            assertThat(awaitItem()).isEmpty()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun cascadeDeleteShoppingList() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()
        val shoppingItemDao = db.shoppingItemDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.addShoppingList(shoppingList)

        val shoppingItem = createShoppingItemEntity()
        shoppingItemDao.addShoppingItem(shoppingItem)

        shoppingItemDao.getAllShoppingItems(DEFAULT_SHOPPING_LIST_ID).test {
            assertThat(awaitItem()).all { hasSize(1); contains(shoppingItem) }

            shoppingListDao.deleteShoppingList(shoppingList)
            assertThat(awaitItem()).isEmpty()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getExistingShoppingListById() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.addShoppingList(shoppingList)

        shoppingListDao.getShoppingListById(DEFAULT_SHOPPING_LIST_ID).test {
            assertThat(awaitItem()).isDataClassEqualTo(shoppingList)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getNonExistingShoppingListById() = runDatabaseTest { db ->
        val shoppingListDao = db.shoppingListDao()

        val shoppingList = createShoppingListEntity()
        shoppingListDao.addShoppingList(shoppingList)

        shoppingListDao.getShoppingListById(-1L).test {
            assertThat(awaitItem()).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
