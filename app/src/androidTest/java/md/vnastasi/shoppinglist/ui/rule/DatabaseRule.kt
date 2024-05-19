package md.vnastasi.shoppinglist.ui.rule

import kotlinx.coroutines.runBlocking
import md.vnastasi.shoppinglist.db.ShoppingListDatabase
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.GlobalContext

fun createDatabaseRule(
    setUp: suspend ShoppingListDatabase.() -> Unit = { },
    cleanUp: suspend ShoppingListDatabase.() -> Unit = { }
): TestRule = DatabaseRule(setUp, cleanUp)

private class DatabaseRule(
    private val setUp: suspend ShoppingListDatabase.() -> Unit = { },
    private val cleanUp: suspend ShoppingListDatabase.() -> Unit = { }
) : TestWatcher() {

    override fun starting(description: Description?) {
        runBlocking {
            setUp.invoke(GlobalContext.get().get())
        }
    }

    override fun finished(description: Description?) {
        runBlocking {
            val shoppingListDatabase = GlobalContext.get().get<ShoppingListDatabase>()
            cleanUp.invoke(shoppingListDatabase)
            shoppingListDatabase.openHelper.writableDatabase.run {
                execSQL("DELETE FROM name_suggestions")
                execSQL("DELETE FROM shopping_items")
                execSQL("DELETE FROM shopping_lists")
            }
        }
    }
}
