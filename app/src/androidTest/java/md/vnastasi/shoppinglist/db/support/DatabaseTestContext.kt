package md.vnastasi.shoppinglist.db.support

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import md.vnastasi.shoppinglist.db.ShoppingListDatabase

private val loggableSqlQueryCallback = RoomDatabase.QueryCallback { sqlQuery, bindArgs ->
    Log.d("DATABASE SQL statement", "$sqlQuery with arguments $bindArgs")
}

fun runDatabaseTest(block: suspend TestScope.(ShoppingListDatabase) -> Unit) = runTest {
    val dispatcher = UnconfinedTestDispatcher(testScheduler)
    Dispatchers.setMain(dispatcher)

    try {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val database = Room.inMemoryDatabaseBuilder(context, ShoppingListDatabase::class.java)
            .setQueryCallback(loggableSqlQueryCallback, dispatcher.asExecutor())
            .build()

        block.invoke(this, database)

        database.close()
    } finally {
        Dispatchers.resetMain()
    }
}
