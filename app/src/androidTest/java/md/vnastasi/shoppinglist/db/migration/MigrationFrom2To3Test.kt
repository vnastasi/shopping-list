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
private const val SELECT_INDICES = "SELECT * FROM sqlite_master WHERE `type` = 'index' and `tbl_name` = 'shopping_items' and name = 'index_shopping_items_list_id'"

class MigrationFrom2To3Test {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        ShoppingListDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrateFrom1To2() {
        helper.createDatabase(TEST_DB_NAME, 2).use { db ->
            db.execSQL(INSERT_SHOPPING_LIST_SQL, arrayOf(1L, "My list"))
        }

        helper.runMigrationsAndValidate(TEST_DB_NAME, 3, true, MigrationFrom2To3()).use { db ->
            db.query(SELECT_INDICES).use { cursor ->
                assertThat(cursor.count).isEqualTo(1)
            }
        }
    }
}
