package md.vnastasi.shoppinglist.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MigrationFrom2To3 : Migration(startVersion = 2, endVersion = 3) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(CREATE_INDEX)
    }

    private companion object {

        const val CREATE_INDEX = "CREATE INDEX IF NOT EXISTS `index_shopping_items_list_id` ON `shopping_items` (`list_id`)"
    }
}
