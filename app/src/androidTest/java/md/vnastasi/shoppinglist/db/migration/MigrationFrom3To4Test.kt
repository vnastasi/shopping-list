package md.vnastasi.shoppinglist.db.migration

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertThat
import assertk.assertions.isEqualTo
import md.vnastasi.shoppinglist.db.ShoppingListDatabase
import org.junit.Rule
import org.junit.Test

private const val TEST_DB_NAME = "testing_migrations_db"
private const val INSERT_SHOPPING_LIST_SQL = "INSERT INTO shopping_lists (`id`, `name`) VALUES (?, ?)"
private const val INSERT_SHOPPING_ITEM_SQL = "INSERT INTO shopping_items (`id`, `name`, `is_checked`, `list_id`) VALUES (?, ?, ?, ?)"
private const val SELECT_SHOPPING_LIST_DETAILS_SQL = "SELECT * FROM `shopping_list_details`"

class MigrationFrom3To4Test {

    @get:Rule
    val helper = MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        databaseClass = ShoppingListDatabase::class.java,
        specs = emptyList()
    )

    @Test
    fun migrateFrom1To2() {
        helper.createDatabase(TEST_DB_NAME, 3).use { db ->
            db.execSQL(INSERT_SHOPPING_LIST_SQL, arrayOf<Any>(1L, "My list"))
            db.execSQL(INSERT_SHOPPING_ITEM_SQL, arrayOf<Any>(1L, "Item 1", 1, 1L))
            db.execSQL(INSERT_SHOPPING_ITEM_SQL, arrayOf<Any>(2L, "Item 2", 0, 1L))
            db.execSQL(INSERT_SHOPPING_ITEM_SQL, arrayOf<Any>(3L, "Item 3", 0, 1L))
        }

        helper.runMigrationsAndValidate(TEST_DB_NAME, 4, true, MigrationFrom3To4()).use { db ->
            db.query(SELECT_SHOPPING_LIST_DETAILS_SQL).use { cursor ->
                assertThat(cursor.count).isEqualTo(1)
                cursor.moveToFirst()
                assertThat(cursor.getLong(0)).isEqualTo(1L)
                assertThat(cursor.getString(1)).isEqualTo("My list")
                assertThat(cursor.getLong(2)).isEqualTo(3L)
                assertThat(cursor.getLong(3)).isEqualTo(1L)
            }
        }
    }
}
