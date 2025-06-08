package md.vnastasi.shoppinglist.db.migration

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import md.vnastasi.shoppinglist.db.ShoppingListDatabase
import md.vnastasi.shoppinglist.db.support.containsAtColumnWithIndex
import org.junit.Rule
import org.junit.Test

private const val TEST_DB_NAME = "testing_migrations_db"
private const val SQL_INSERT_SHOPPING_LIST = "INSERT INTO `shopping_lists` (`id`, `name`) VALUES (?, ?)"
private const val SQL_INSERT_SHOPPING_ITEM = "INSERT INTO `shopping_items` (`id`, `name`, `is_checked`, `list_id`) VALUES (?, ?, ?, ?)"
private const val SQL_SELECT_NAME_SUGGESTIONS = "SELECT * FROM `name_suggestions`"

class MigrationFrom4To5Test {

    @get:Rule
    val helper = MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        databaseClass = ShoppingListDatabase::class.java,
        specs = emptyList(),
        openFactory = FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrateFrom4To5() {
        helper.createDatabase(TEST_DB_NAME, 4)

        helper.runMigrationsAndValidate(TEST_DB_NAME, 5, true, MigrationFrom4To5()).use { db ->
            db.execSQL(SQL_INSERT_SHOPPING_LIST, arrayOf<Any>(1L, "My list"))
            db.execSQL(SQL_INSERT_SHOPPING_ITEM, arrayOf<Any>(1L, "Bread", 0, 1L))
            db.execSQL(SQL_INSERT_SHOPPING_ITEM, arrayOf<Any>(2L, "Toothpaste", 0, 1L))
            db.execSQL(SQL_INSERT_SHOPPING_ITEM, arrayOf<Any>(3L, "Butter", 0, 1L))

            // Verify trigger inserted name suggestions successfully
            db.query(SQL_SELECT_NAME_SUGGESTIONS).use { cursor ->
                cursor.containsAtColumnWithIndex(1, "Bread", "Toothpaste", "Butter")
            }
        }
    }
}
