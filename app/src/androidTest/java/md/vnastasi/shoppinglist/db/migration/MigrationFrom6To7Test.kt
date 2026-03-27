package md.vnastasi.shoppinglist.db.migration

import androidx.room.testing.MigrationTestHelper
import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertThat
import assertk.assertions.isEqualTo
import md.vnastasi.shoppinglist.db.ShoppingListDatabase
import org.junit.Rule
import org.junit.Test

private const val TEST_DB_NAME = "testing_migrations_db"
private const val SQL_INSERT_SHOPPING_LIST = "INSERT INTO shopping_lists (id, name, position) VALUES (?, ?, ?)"
private const val SQL_INSERT_SHOPPING_ITEM = "INSERT INTO shopping_items (id, name, is_checked, list_id) VALUES (?, ?, ?, ?)"
private const val SQL_SELECT_SHOPPING_ITEM = "SELECT * FROM shopping_items"

private const val COL_ID = "id"
private const val COL_NAME = "name"
private const val COL_POSITION = "position"
private const val COL_LIST_ID = "list_id"

class MigrationFrom6To7Test {

    @get:Rule
    val helper = MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        databaseClass = ShoppingListDatabase::class.java,
        specs = emptyList()
    )

    @Test
    fun migrateFrom6To7() {
        helper.createDatabase(TEST_DB_NAME, 6).use { db ->
            db.execSQL(SQL_INSERT_SHOPPING_LIST, arrayOf<Any>(1L, "List 1", 1L))
            db.execSQL(SQL_INSERT_SHOPPING_LIST, arrayOf<Any>(2L, "List 2", 2L))
            db.execSQL(SQL_INSERT_SHOPPING_ITEM, arrayOf<Any>(1L, "Item 1", 0, 1L))
            db.execSQL(SQL_INSERT_SHOPPING_ITEM, arrayOf<Any>(2L, "Item 2", 0, 1L))
            db.execSQL(SQL_INSERT_SHOPPING_ITEM, arrayOf<Any>(3L, "Item 3", 0, 2L))
            db.execSQL(SQL_INSERT_SHOPPING_ITEM, arrayOf<Any>(4L, "Item 4", 0, 1L))
            db.execSQL(SQL_INSERT_SHOPPING_ITEM, arrayOf<Any>(5L, "Item 5", 0, 2L))
        }

        helper.runMigrationsAndValidate(TEST_DB_NAME, 7, true, MigrationFrom6To7()).use { db ->
            // Verify positions were updated after migration
            db.query(SQL_SELECT_SHOPPING_ITEM).use { cursor ->
                cursor.moveToFirst()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(1L)
                assertThat(cursor.getString(cursor.getColumnIndex(COL_NAME))).isEqualTo("Item 1")
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(1L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_LIST_ID))).isEqualTo(1L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(2L)
                assertThat(cursor.getString(cursor.getColumnIndex(COL_NAME))).isEqualTo("Item 2")
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(2L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_LIST_ID))).isEqualTo(1L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(3L)
                assertThat(cursor.getString(cursor.getColumnIndex(COL_NAME))).isEqualTo("Item 3")
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(3L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_LIST_ID))).isEqualTo(2L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(4L)
                assertThat(cursor.getString(cursor.getColumnIndex(COL_NAME))).isEqualTo("Item 4")
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(4L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_LIST_ID))).isEqualTo(1L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(5L)
                assertThat(cursor.getString(cursor.getColumnIndex(COL_NAME))).isEqualTo("Item 5")
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(5L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_LIST_ID))).isEqualTo(2L)
            }

            //Verify trigger is working
            db.execSQL(SQL_INSERT_SHOPPING_ITEM, arrayOf<Any>(6L, "Item 6", 0, 1L))
            db.execSQL(SQL_INSERT_SHOPPING_ITEM, arrayOf<Any>(7L, "Item 7", 0, 2L))
            db.execSQL(SQL_INSERT_SHOPPING_ITEM, arrayOf<Any>(8L, "Item 8", 0, 2L))
            db.execSQL(SQL_INSERT_SHOPPING_ITEM, arrayOf<Any>(9L, "Item 9", 0, 1L))

            db.query(SQL_SELECT_SHOPPING_ITEM).use { cursor ->
                cursor.moveToFirst()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(1L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(1L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(2L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(2L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(3L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(3L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(4L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(4L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(5L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(5L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(6L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(5L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(7L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(6L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(8L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(7L)

                cursor.moveToNext()
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_ID))).isEqualTo(9L)
                assertThat(cursor.getLong(cursor.getColumnIndex(COL_POSITION))).isEqualTo(6L)
            }
        }
    }
}
