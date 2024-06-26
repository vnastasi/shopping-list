package md.vnastasi.shoppinglist.domain.repository

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.db.TestData.createShoppingListDetailsView
import md.vnastasi.shoppinglist.db.TestData.createShoppingListEntity
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.domain.TestData.createShoppingList
import md.vnastasi.shoppinglist.domain.TestData.createShoppingListDetails
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import md.vnastasi.shoppinglist.db.model.ShoppingList as ShoppingListEntity

internal class LocalShoppingListRepositoryTest {

    private val mockShoppingListDao = mock<ShoppingListDao>()

    private val shoppingListRepository = LocalShoppingListRepository(mockShoppingListDao)

    @Test
    @DisplayName("Given no entities in DAO When getting available shopping lists Then expect empty list")
    fun getAvailableListsEmpty() = runTest {
        whenever(mockShoppingListDao.findAll()).doReturn(flowOf(emptyList()))

        shoppingListRepository.findAll().test {
            assertThat(awaitItem()).isEmpty()
            awaitComplete()
        }
    }

    @Test
    @DisplayName("Given existing entities in DAO When getting available shopping lists Then expect non-empty list")
    fun getAvailableLists() = runTest {
        val shoppingListDetailsView1 = createShoppingListDetailsView {
            id = 1L
            name = "Praxis"
            totalItems = 10L
            checkedItems = 3L
        }
        val shoppingListDetailsView2 = createShoppingListDetailsView {
            id = 2L
            name = "Jumbo"
            totalItems = 1L
            checkedItems = 1L
        }
        whenever(mockShoppingListDao.findAll()).doReturn(flowOf(listOf(shoppingListDetailsView1, shoppingListDetailsView2)))

        val expectedList = listOf(
            createShoppingListDetails {
                id = 1L
                name = "Praxis"
                totalItems = 10L
                checkedItems = 3L
            },
            createShoppingListDetails {
                id = 2L
                name = "Jumbo"
                totalItems = 1L
                checkedItems = 1L
            }
        )

        shoppingListRepository.findAll().test {
            assertThat(awaitItem()).isEqualTo(expectedList)
            awaitComplete()
        }
    }

    @Test
    @DisplayName("When creating a new shopping list The expect DAO call to create new entity")
    fun create() = runTest {
        val shoppingList = createShoppingList {
            id = 5678L
            name = "New List"
        }

        val expectedShoppingListEntity = createShoppingListEntity {
            id = 5678L
            name = "New List"
        }

        shoppingListRepository.create(shoppingList)

        argumentCaptor<ShoppingListEntity> {
            verify(mockShoppingListDao).create(capture())
            assertThat(firstValue).isDataClassEqualTo(expectedShoppingListEntity)
        }
    }

    @Test
    @DisplayName("When updating an existing shopping list The expect DAO call to update entity")
    fun update() = runTest {
        val shoppingList = createShoppingList {
            id = 5678L
            name = "New List"
        }

        val expectedShoppingListEntity = createShoppingListEntity {
            id = 5678L
            name = "New List"
        }

        shoppingListRepository.update(shoppingList)

        argumentCaptor<ShoppingListEntity> {
            verify(mockShoppingListDao).update(capture())
            assertThat(firstValue).isDataClassEqualTo(expectedShoppingListEntity)
        }
    }

    @Test
    @DisplayName("When deleting a shopping list The expect DAO call to delete entity")
    fun delete() = runTest {
        val shoppingList = createShoppingList {
            id = 5678L
            name = "New List"
        }

        val expectedShoppingListEntity = createShoppingListEntity {
            id = 5678L
            name = "New List"
        }

        shoppingListRepository.delete(shoppingList)

        argumentCaptor<ShoppingListEntity> {
            verify(mockShoppingListDao).delete(capture())
            assertThat(firstValue).isDataClassEqualTo(expectedShoppingListEntity)
        }
    }
}
