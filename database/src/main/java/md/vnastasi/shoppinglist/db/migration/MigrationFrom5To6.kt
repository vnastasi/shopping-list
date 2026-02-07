package md.vnastasi.shoppinglist.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import md.vnastasi.shoppinglist.db.callback.CreateAutoPositionShoppingListTriggerCallback

class MigrationFrom5To6 : Migration(startVersion = 5, endVersion = 6) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(ALTER_TABLE)
        db.execSQL(UPDATE_POSITIONS)
        db.execSQL(DROP_VIEW)
        db.execSQL(RECREATE_VIEW)
        db.execSQL(CreateAutoPositionShoppingListTriggerCallback.SQL_STATEMENT)
    }

    private companion object {

        const val ALTER_TABLE = "ALTER TABLE shopping_lists ADD COLUMN position INTEGER NOT NULL DEFAULT 0"

        const val UPDATE_POSITIONS = "UPDATE shopping_lists SET position = id"

        const val DROP_VIEW = "DROP VIEW IF EXISTS shopping_list_details"

        const val RECREATE_VIEW =
            "CREATE VIEW `shopping_list_details` AS " +
                    "SELECT " +
                    "list.id AS id, " +
                    "list.name AS name, " +
                    "list.position AS position, " +
                    "(SELECT count(*) FROM shopping_items item WHERE item.list_id = list.id) AS totalItems, " +
                    "(SELECT count(*) FROM shopping_items item WHERE item.list_id = list.id AND item.is_checked = 1) AS checkedItems " +
                    "FROM shopping_lists list " +
                    "ORDER BY list.position ASC"
    }
}
