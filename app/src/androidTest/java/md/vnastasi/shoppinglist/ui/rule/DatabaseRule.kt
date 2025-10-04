package md.vnastasi.shoppinglist.ui.rule

import androidx.test.core.app.ApplicationProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.OnComponentReadyRunner
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import md.vnastasi.shoppinglist.db.ShoppingListDatabase
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

fun databaseRule(
    onSetUp: suspend ShoppingListDatabase.() -> Unit = {},
    onTearDown: suspend ShoppingListDatabase.() -> Unit = {}
): TestRule = DatabaseRule(onSetUp, onTearDown)

private class DatabaseRule(
    private val onSetUp: suspend ShoppingListDatabase.() -> Unit,
    private val onTearDown: suspend ShoppingListDatabase.() -> Unit
) : TestWatcher() {

    private val application: HiltTestApplication
        get() = ApplicationProvider.getApplicationContext()

    private lateinit var shoppingListDatabase: ShoppingListDatabase

    override fun starting(description: Description?) {
        super.starting(description)
        OnComponentReadyRunner.addListener(application, TestRulEntryPoint::class.java) { entryPoint ->
            shoppingListDatabase = entryPoint.shoppingListDatabase

            runBlocking {
                with(shoppingListDatabase) {
                    truncate()
                    onSetUp.invoke(this)
                }
            }
        }
    }

    override fun finished(description: Description?) {
        runBlocking {
            with(shoppingListDatabase) {
                onTearDown.invoke(this)
                truncate()
            }
        }
        super.finished(description)
    }

    private fun ShoppingListDatabase.truncate() {
        openHelper.writableDatabase.run {
            execSQL("DELETE FROM name_suggestions")
            execSQL("DELETE FROM shopping_items")
            execSQL("DELETE FROM shopping_lists")
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface TestRulEntryPoint {

        val shoppingListDatabase: ShoppingListDatabase
    }
}
