package md.vnastasi.shoppinglist.domain.repository

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEqualTo
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDao
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.support.testdata.DbTestData.createShoppingItemEntity
import md.vnastasi.shoppinglist.support.testdata.DbTestData.createShoppingListEntity
import md.vnastasi.shoppinglist.support.testdata.DomainTestData.createShoppingItem
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import md.vnastasi.shoppinglist.db.model.ShoppingItem as ShoppingItemEntity

class ShoppingItemRepositoryTest {

    private val mockShoppingListDao = mock<ShoppingListDao>()
    private val mockShoppingItemDao = mock<ShoppingItemDao>()

    private val shoppingItemRepository = ShoppingItemRepository(mockShoppingListDao, mockShoppingItemDao)

    @Test
    @DisplayName("Given no shopping item entities in DAO When getting shopping items for list 123 The expect empty list")
    fun getAllItemsEmpty() = runTest {
        val listId = 123L
        whenever(mockShoppingListDao.getShoppingListById(listId)).doReturn(flowOf(createShoppingListEntity()))
        whenever(mockShoppingItemDao.getAllShoppingItems(listId)).doReturn(emptyFlow())

        shoppingItemRepository.getAllItems(listId).test {
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
        whenever(mockShoppingListDao.getShoppingListById(shoppingListId)).doReturn(flowOf(shoppingListEntity))
        whenever(mockShoppingItemDao.getAllShoppingItems(shoppingListId)).doReturn(flowOf(listOf(shoppingItemEntity1, shoppingItemEntity2)))

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

        shoppingItemRepository.getAllItems(shoppingListId).test {
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

        shoppingItemRepository.create(shoppingItem)

        argumentCaptor<ShoppingItemEntity> {
            verify(mockShoppingItemDao).addShoppingItem(capture())
            assertThat(firstValue).isDataClassEqualTo(expectedShoppingItemEntity)
        }
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

        shoppingItemRepository.update(shoppingItem)

        argumentCaptor<ShoppingItemEntity> {
            verify(mockShoppingItemDao).updateShoppingItem(capture())
            assertThat(firstValue).isDataClassEqualTo(expectedShoppingItemEntity)
        }
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

        shoppingItemRepository.delete(shoppingItem)

        argumentCaptor<ShoppingItemEntity> {
            verify(mockShoppingItemDao).deleteShoppingItem(capture())
            assertThat(firstValue).isDataClassEqualTo(expectedShoppingItemEntity)
        }
    }
}
