package md.vnastasi.shoppinglist.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MigrationFrom1To2 : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(CREATE_NEW_TABLE_SQL)
        db.execSQL(CREATE_INDEX)
        db.execSQL(TRANSFER_EXISTING_NAMES_SQL)
    }

    private companion object {

        private val CREATE_NEW_TABLE_SQL = """
        CREATE TABLE IF NOT EXISTS `name_suggestions` (
            `id` INTEGER NOT NULL, 
            `value` TEXT NOT NULL COLLATE NOCASE, 
            PRIMARY KEY(`id`)
        );
    """.trimIndent()

        private const val CREATE_INDEX = "CREATE UNIQUE INDEX IF NOT EXISTS `idx_name_suggestions_value` ON `name_suggestions` (`value`)"

        private val TRANSFER_EXISTING_NAMES_SQL = """
        INSERT INTO `name_suggestions` (`value`)
            SELECT `name` FROM shopping_items;
    """.trimIndent()
    }
}
