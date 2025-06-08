package md.vnastasi.shoppinglist.db.callback

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

internal class CreateNameSuggestionTriggerCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        db.execSQL(SQL_STATEMENT)
    }

    companion object {

        const val SQL_STATEMENT = """
            CREATE TRIGGER `create_name_suggestion_trigger` IF NOT EXISTS
                AFTER INSERT ON `shopping_items`
                FOR EACH ROW
            BEGIN
                INSERT OR REPLACE INTO `name_suggestions` (`value`) 
                VALUES (trim(NEW.`name`))
            END;
        """
    }
}