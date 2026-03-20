package md.vnastasi.shoppinglist.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import md.vnastasi.shoppinglist.db.callback.CreateAutoPositionShoppingItemTriggerCallback

class MigrationFrom6To7 : Migration(startVersion = 6, endVersion = 7) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(ALTER_TABLE)
        db.execSQL(UPDATE_POSITIONS)
        db.execSQL(CreateAutoPositionShoppingItemTriggerCallback.SQL_STATEMENT)
    }

    private companion object {

        const val ALTER_TABLE = "ALTER TABLE shopping_items ADD COLUMN position INTEGER NOT NULL DEFAULT 0"

        const val UPDATE_POSITIONS = "UPDATE shopping_items SET position = id"
    }
}
