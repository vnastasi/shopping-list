package md.vnastasi.shoppinglist.db.support

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import md.vnastasi.shoppinglist.db.ShoppingListDatabase
import md.vnastasi.shoppinglist.db.callback.CreateNameSuggestionTriggerCallback

private val loggableSqlQueryCallback = RoomDatabase.QueryCallback { sqlQuery, bindArgs ->
    Log.d("DATABASE SQL statement", "$sqlQuery with arguments $bindArgs")
}

fun runDatabaseTest(block: suspend TestScope.(ShoppingListDatabase) -> Unit) = runTest {
    val context = ApplicationProvider.getApplicationContext<Application>()
    val executor = UnconfinedTestDispatcher(testScheduler).asExecutor()
    val database = Room.inMemoryDatabaseBuilder(context, ShoppingListDatabase::class.java)
        .setQueryExecutor(executor)
        .setTransactionExecutor(executor)
        .setQueryCallback(loggableSqlQueryCallback, executor)
        .addCallback(CreateNameSuggestionTriggerCallback())
        .build()

    try {
        block.invoke(this, database)
    } finally {
        database.close()
    }
}
