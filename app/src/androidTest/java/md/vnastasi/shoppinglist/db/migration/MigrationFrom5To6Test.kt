package md.vnastasi.shoppinglist.db.migration

import androidx.room.testing.MigrationTestHelper
import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertThat
import assertk.assertions.isEqualTo
import md.vnastasi.shoppinglist.db.ShoppingListDatabase
import org.junit.Rule
import org.junit.Test

private const val TEST_DB_NAME = "testing_migrations_db"
private const val SQL_INSERT_SHOPPING_LIST = "INSERT INTO shopping_lists (id, name) VALUES (?, ?)"
private const val SQL_SELECT_SHOPPING_LIST_DETAILS = "SELECT * FROM shopping_list_details"

private const val COL_ID = "id"
private const val COL_NAME = "name"
private const val COL_POSITION = "position"

class MigrationFrom5To6Test {

    @get:Rule
    val helper = MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        databaseClass = ShoppingListDatabase::class.java,
        specs = emptyList()
    )

    @Test
    fun migrateFrom5To6() {
        helper.createDatabase(TEST_DB_NAME, 5).use { db ->
            db.execSQL(SQL_INSERT_SHOPPING_LIST, arrayOf<Any>(1L, "List 1"))
            db.execSQL(SQL_INSERT_SHOPPING_LIST, arrayOf<Any>(2L, "List 2"))
            db.execSQL(SQL_INSERT_SHOPPING_LIST, arrayOf<Any>(3L, "List 3"))
        }

        helper.runMigrationsAndValidate(TEST_DB_NAME, 6, true, MigrationFrom5To6()).use { db ->
            // Verify position was updated after migration
            db.query(SQL_SELECT_SHOPPING_LIST_DETAILS).use { cursor ->
                cursor.moveToFirst()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(1L)
                assertThat(cursor.getString(cursor.getColumnIndex(COL_NAME))).isEqualTo("List 1")
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(1L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(2L)
                assertThat(cursor.getString(cursor.getColumnIndex(COL_NAME))).isEqualTo("List 2")
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(2L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(3L)
                assertThat(cursor.getString(cursor.getColumnIndex(COL_NAME))).isEqualTo("List 3")
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(3L)

                cursor.moveToNext()
            }

            // Verify trigger is working
            db.execSQL(SQL_INSERT_SHOPPING_LIST, arrayOf<Any>(4L, "List 4"))
            db.query(SQL_SELECT_SHOPPING_LIST_DETAILS).use { cursor ->
                cursor.moveToPosition(3)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(4L)
                assertThat(cursor.getString(cursor.getColumnIndex(COL_NAME))).isEqualTo("List 4")
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(4L)
            }
        }
    }
}
