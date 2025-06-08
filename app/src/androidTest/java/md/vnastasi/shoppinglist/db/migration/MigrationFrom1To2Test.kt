package md.vnastasi.shoppinglist.db.migration

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import md.vnastasi.shoppinglist.db.ShoppingListDatabase
import md.vnastasi.shoppinglist.db.support.containsAtColumnWithIndex
import org.junit.Rule
import org.junit.Test

private const val TEST_DB_NAME = "testing_migrations_db"
private const val INSERT_SHOPPING_LIST_SQL = "INSERT INTO `shopping_lists` (`id`, `name`) VALUES (?, ?)"
private const val INSERT_SHOPPING_LIST_ITEM_SQL = "INSERT INTO `shopping_items` (`id`, `name`, `is_checked`, `list_id`) VALUES (?, ?, ?, ?)"
private const val SELECT_SUGGESTIONS = "SELECT `value` FROM `name_suggestions`"

class MigrationFrom1To2Test {

    @get:Rule
    val helper = MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        databaseClass = ShoppingListDatabase::class.java,
        specs = emptyList()
    )

    @Test
    fun migrateFrom1To2() {
        helper.createDatabase(TEST_DB_NAME, 1).use { db ->
            db.execSQL(INSERT_SHOPPING_LIST_SQL, arrayOf<Any>(1L, "My list"))
            db.execSQL(INSERT_SHOPPING_LIST_ITEM_SQL, arrayOf<Any>(1L, "Burger", 1, 1L))
            db.execSQL(INSERT_SHOPPING_LIST_ITEM_SQL, arrayOf<Any>(2L, "Quark", 0, 1L))
            db.execSQL(INSERT_SHOPPING_LIST_ITEM_SQL, arrayOf<Any>(3L, "Bread", 0, 1L))
        }

        helper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MigrationFrom1To2()).use { db ->
            // Verify copying existing values worked
            db.query(SELECT_SUGGESTIONS).use { cursor ->
                cursor.containsAtColumnWithIndex(0, "Burger", "Quark", "Bread")
            }
        }
    }
}
