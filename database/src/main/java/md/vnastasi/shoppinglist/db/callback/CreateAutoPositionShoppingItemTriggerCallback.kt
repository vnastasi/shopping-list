package md.vnastasi.shoppinglist.db.callback

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class CreateAutoPositionShoppingItemTriggerCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        db.execSQL(SQL_STATEMENT)
    }

    companion object {

        const val SQL_STATEMENT = """
            CREATE TRIGGER IF NOT EXISTS trigger_auto_position_shopping_items
                AFTER INSERT ON shopping_items
                FOR EACH ROW
            BEGIN
                UPDATE shopping_items
                SET position = (
                    SELECT MAX(position) + 1
                    FROM shopping_items
                    WHERE list_id = NEW.list_id
                )
                WHERE id = NEW.id;
            END;
        """
    }
}
