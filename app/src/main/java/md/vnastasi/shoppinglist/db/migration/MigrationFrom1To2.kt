package md.vnastasi.shoppinglist.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MigrationFrom1To2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(CREATE_NEW_TABLE_SQL)
        database.execSQL(CREATE_INDEX)
        database.execSQL(TRANSFER_EXISTING_NAMES_SQL)
        database.execSQL(CREATE_TRIGGER_SQL)
    }

    private companion object {

        private val CREATE_NEW_TABLE_SQL = """
        CREATE TABLE IF NOT EXISTS `shopping_item_name_suggestions` (
            `id` INTEGER NOT NULL, 
            `value` TEXT NOT NULL COLLATE NOCASE, 
            PRIMARY KEY(`id`)
        );
    """.trimIndent()

        private val CREATE_INDEX = "CREATE UNIQUE INDEX IF NOT EXISTS `idx_shopping_item_name_suggestions_value` ON `shopping_item_name_suggestions` (`value`)"

        private val TRANSFER_EXISTING_NAMES_SQL = """
        INSERT INTO shopping_item_name_suggestions (`value`)
            SELECT `name` FROM shopping_items;
    """.trimIndent()

        private val CREATE_TRIGGER_SQL = """
        CREATE TRIGGER IF NOT EXISTS add_suggestion
            AFTER INSERT ON shopping_items 
        FOR EACH ROW
        BEGIN
            INSERT OR IGNORE INTO shopping_item_name_suggestions (`value`) VALUES (new.name);
        END;
    """.trimIndent()
    }
}
