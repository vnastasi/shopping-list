package md.vnastasi.shoppinglist.db.migration

import android.database.Cursor
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
private const val INSERT_SHOPPING_LIST_ITEM_SQL = "INSERT INTO shopping_items (`id`, `name`, `is_checked`, `list_id`) VALUES (?, ?, ?, ?)"
private const val SELECT_SUGGESTIONS = "SELECT `value` FROM name_suggestions"

class MigrationFrom1To2Test {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        ShoppingListDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrateFrom1To2() {
        helper.createDatabase(TEST_DB_NAME, 1).use { db ->
            db.execSQL(INSERT_SHOPPING_LIST_SQL, arrayOf(1L, "My list"))
            db.execSQL(INSERT_SHOPPING_LIST_ITEM_SQL, arrayOf(1L, "Burger", 1, 1L))
            db.execSQL(INSERT_SHOPPING_LIST_ITEM_SQL, arrayOf(2L, "Quark", 0, 1L))
            db.execSQL(INSERT_SHOPPING_LIST_ITEM_SQL, arrayOf(3L, "Bread", 0, 1L))
        }

        helper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MigrationFrom1To2()).use { db ->
            // Verify copying existing values worked
            db.query(SELECT_SUGGESTIONS).use { cursor ->
                cursor.containsAtIndex(0, "Burger", "Quark", "Bread")
            }
        }
    }


    private fun Cursor.containsAtIndex(index: Int, vararg values: String) {
        assertThat(this.count).isEqualTo(values.size)
        repeat(values.size) {
            moveToNext()
            assertThat(getString(index)).isEqualTo(values[it])
        }
    }
}
