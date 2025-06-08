package md.vnastasi.shoppinglist.db.callback

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class CreateNameSuggestionTriggerCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        db.execSQL(SQL_STATEMENT)
    }

    companion object {

        const val SQL_STATEMENT = """
            CREATE TRIGGER IF NOT EXISTS `trigger_create_name_suggestion`
                AFTER INSERT ON `shopping_items`
                FOR EACH ROW
            BEGIN
                INSERT INTO `name_suggestions` (`value`) 
                VALUES (trim(NEW.`name`))
                ON CONFLICT (`value`) DO NOTHING;
            END;
        """
    }
}
