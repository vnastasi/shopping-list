package md.vnastasi.shoppinglist.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import md.vnastasi.shoppinglist.db.callback.CreateNameSuggestionTriggerCallback

class MigrationFrom4To5 : Migration(startVersion = 4, endVersion = 5) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(CreateNameSuggestionTriggerCallback.SQL_STATEMENT)
    }
}
