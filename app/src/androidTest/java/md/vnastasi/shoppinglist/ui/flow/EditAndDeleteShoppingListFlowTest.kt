package md.vnastasi.shoppinglist.ui.flow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.db.model.ShoppingList
import md.vnastasi.shoppinglist.ui.robot.manageShoppingListSheet
import md.vnastasi.shoppinglist.ui.robot.overviewScreen
import md.vnastasi.shoppinglist.ui.rule.databaseRule
import md.vnastasi.shoppinglist.ui.rule.disableAnimationsRule
import md.vnastasi.shoppinglist.ui.rule.retryOnFailureRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

@HiltAndroidTest
class EditAndDeleteShoppingListFlowTest {

    private val hiltAndroidRule = HiltAndroidRule(this)

    private val composeRule = createAndroidComposeRule<MainActivity>()

    private val databaseRule = databaseRule(
        onSetUp = {
            val shoppingListDao = shoppingListDao()

            shoppingListDao.create(ShoppingList(1L, "Groceries"))
            shoppingListDao.create(ShoppingList(2L, "Gardening"))
        }
    )

    @get:Rule
    val ruleChain: TestRule = RuleChain
        .outerRule(hiltAndroidRule)
        .around(databaseRule)
        .around(composeRule)
        .around(retryOnFailureRule(maxAttempts = 3))
        .around(disableAnimationsRule())

    @Test
    fun editShoppingList() {
        overviewScreen(composeRule) {
            swipeShoppingListCard("Groceries")
            clickOnEditShoppingList()
        }

        manageShoppingListSheet(composeRule) {
            hasPrefilledShoppingListName("Groceries")
            typeShoppingListName("New name")
            clickOnSaveButton()
        }

        overviewScreen(composeRule) {
            hasShoppingListCard("New name", 0, 0)
        }
    }

    @Test
    fun deleteShoppingList() {
        overviewScreen(composeRule) {
            swipeShoppingListCard("Gardening")
            clickOnDeleteShoppingList()
            hasNoShoppingListCard("Gardening")
        }
    }
}
