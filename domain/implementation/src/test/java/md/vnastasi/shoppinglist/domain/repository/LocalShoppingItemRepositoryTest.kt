package md.vnastasi.shoppinglist.domain.repository

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEqualTo
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.db.TestData.createShoppingItemEntity
import md.vnastasi.shoppinglist.db.TestData.createShoppingListEntity
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDao
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingItem
import md.vnastasi.shoppinglist.domain.model.TestData.createShoppingList
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import md.vnastasi.shoppinglist.db.model.ShoppingItem as ShoppingItemEntity

internal class LocalShoppingItemRepositoryTest {

    private val mockShoppingListDao = mockk<ShoppingListDao>(relaxUnitFun = true)
    private val mockShoppingItemDao = mockk<ShoppingItemDao>(relaxUnitFun = true)

    private val shoppingItemRepository = LocalShoppingItemRepository(mockShoppingListDao, mockShoppingItemDao)

    @Test
    @DisplayName("Given no shopping item entities in DAO When getting shopping items for list 123 The expect empty list")
    fun getAllItemsEmpty() = runTest {
        val listId = 123L
        coEvery { mockShoppingListDao.findById(listId) } returns flowOf(createShoppingListEntity())
        coEvery { mockShoppingItemDao.findAll(listId) } returns emptyFlow()

        shoppingItemRepository.findAll(listId).test {
            awaitComplete()
        }
    }

    @Test
    @DisplayName("Given no shopping item entities in DAO When getting shopping items for list 45 The expect non-empty list")
    fun getAllItems() = runTest {
        val shoppingListId = 45L
        val shoppingListEntity = createShoppingListEntity {
            id = shoppingListId
            name = "Bakery"
        }
        val shoppingItemEntity1 = createShoppingItemEntity {
            id = 56L
            name = "Bread"
            isChecked = true
            listId = shoppingListId
        }
        val shoppingItemEntity2 = createShoppingItemEntity {
            id = 54L
            name = "Cupcakes"
            isChecked = false
            listId = shoppingListId
        }
        coEvery { mockShoppingListDao.findById(shoppingListId) } returns flowOf(shoppingListEntity)
        coEvery { mockShoppingItemDao.findAll(shoppingListId) } returns flowOf(listOf(shoppingItemEntity1, shoppingItemEntity2))

        val expectedListOfShoppingItems = listOf(
            createShoppingItem {
                id = 56L
                name = "Bread"
                isChecked = true
                shoppingList = {
                    id = shoppingListId
                    name = "Bakery"
                }
            },
            createShoppingItem {
                id = 54L
                name = "Cupcakes"
                isChecked = false
                shoppingList = {
                    id = shoppingListId
                    name = "Bakery"
                }
            }
        )

        shoppingItemRepository.findAll(shoppingListId).test {
            val listOfShoppingItems = awaitItem()
            assertThat(listOfShoppingItems).isEqualTo(expectedListOfShoppingItems)
            awaitComplete()
        }
    }

    @Test
    @DisplayName("When creating a new shopping item The expect DAO call to create new entity")
    fun create() = runTest {
        val shoppingItem = createShoppingItem {
            id = 980L
            name = "Tomatoes"
            isChecked = false
            shoppingList = {
                id = 18L
                name = "Groceries"
            }
        }

        val expectedShoppingItemEntity = createShoppingItemEntity {
            id = 980L
            name = "Tomatoes"
            isChecked = false
            listId = 18L
        }

        val shoppingItemSlot = slot<ShoppingItemEntity>()
        coEvery { mockShoppingItemDao.create(capture(shoppingItemSlot)) } returns 0L

        shoppingItemRepository.create(shoppingItem)

        assertThat(shoppingItemSlot.captured).isDataClassEqualTo(expectedShoppingItemEntity)
    }

    @Test
    @DisplayName("When updating an existing shopping item The expect DAO call to update entity")
    fun update() = runTest {
        val shoppingItem = createShoppingItem {
            id = 56L
            name = "Bread"
            isChecked = true
            shoppingList = {
                id = 1L
                name = "Bakery"
            }
        }

        val expectedShoppingItemEntity = createShoppingItemEntity {
            id = 56L
            name = "Bread"
            isChecked = true
            listId = 1L
        }

        val shoppingItemSlot = slot<ShoppingItemEntity>()
        coEvery { mockShoppingItemDao.update(capture(shoppingItemSlot)) } returns Unit

        shoppingItemRepository.update(shoppingItem)

        assertThat(shoppingItemSlot.captured).isDataClassEqualTo(expectedShoppingItemEntity)
    }

    @Test
    @DisplayName("When deleting a shopping item The expect DAO call to delete entity")
    fun delete() = runTest {
        val shoppingItem = createShoppingItem {
            id = 89L
            name = "Almonds"
            isChecked = true
            shoppingList = {
                id = 67L
                name = "Pindakaas winkel"
            }
        }

        val expectedShoppingItemEntity = createShoppingItemEntity {
            id = 89L
            name = "Almonds"
            isChecked = true
            listId = 67L
        }

        val shoppingItemSlot = slot<ShoppingItemEntity>()
        coEvery { mockShoppingItemDao.delete(capture(shoppingItemSlot)) } returns Unit

        shoppingItemRepository.delete(shoppingItem)

        assertThat(shoppingItemSlot.captured).isDataClassEqualTo(expectedShoppingItemEntity)
    }

    @Test
    @DisplayName("When reordering shopping items The expect new order to be applied")
    fun reorder() = runTest {
        val list = createShoppingList()

        val shoppingItemA = createShoppingItem {
            id = 1L
            name = "A"
            shoppingList = {
                id = list.id
                name = list.name
            }
        }
        val shoppingItemB = createShoppingItem {
            id = 2L
            name = "B"
            shoppingList = {
                id = list.id
                name = list.name
            }
        }
        val shoppingItemC = createShoppingItem {
            id = 3L
            name = "C"
            shoppingList = {
                id = list.id
                name = list.name
            }
        }

        val listSlot = slot<List<ShoppingItemEntity>>()
        coEvery { mockShoppingItemDao.reorder(capture(listSlot)) } returns Unit

        shoppingItemRepository.reorder(listOf(shoppingItemB, shoppingItemA, shoppingItemC))

        val expectedEntityA = createShoppingItemEntity {
            id = 0L
            name = "A"
            listId = list.id
        }
        val expectedEntityB = createShoppingItemEntity {
            id = 0L
            name = "B"
            listId = list.id
        }
        val expectedEntityC = createShoppingItemEntity {
            id = 0L
            name = "C"
            listId = list.id
        }

        assertThat(listSlot.captured).containsExactly(expectedEntityC, expectedEntityA, expectedEntityB)
    }
}
