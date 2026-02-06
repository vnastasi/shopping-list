package md.vnastasi.shoppinglist.db.callback

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class CreateAutoPositionShoppingListTriggerCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        db.execSQL(SQL_STATEMENT)
    }

    companion object {

        const val SQL_STATEMENT = """
            CREATE TRIGGER IF NOT EXISTS trigger_auto_position_shopping_lists
                AFTER INSERT ON shopping_lists
                FOR EACH ROW
            BEGIN
                UPDATE shopping_lists
                SET position = (
                    SELECT MAX(position) + 1
                    FROM shopping_lists
                )
                WHERE id = NEW.id;
            END;
        """
    }
}
