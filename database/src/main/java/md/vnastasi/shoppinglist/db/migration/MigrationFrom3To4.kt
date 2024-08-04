package md.vnastasi.shoppinglist.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MigrationFrom3To4 : Migration(startVersion = 3, endVersion = 4) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(CREATE_VIEW)
    }

    private companion object {

        const val CREATE_VIEW =
            "CREATE VIEW `shopping_list_details` AS " +
                    "SELECT " +
                    "list.id AS id, l" +
                    "ist.name AS name, " +
                    "(SELECT count(*) FROM shopping_items item WHERE item.list_id = list.id) AS totalItems, " +
                    "(SELECT count(*) FROM shopping_items item WHERE item.list_id = list.id AND item.is_checked = 1) AS checkedItems " +
                    "FROM shopping_lists list"
    }
}
