package md.vnastasi.shoppinglist.ui.flow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.db.model.ShoppingItem
import md.vnastasi.shoppinglist.db.model.ShoppingList
import md.vnastasi.shoppinglist.support.async.DispatchersProvider
import md.vnastasi.shoppinglist.ui.robot.listDetailsScreen
import md.vnastasi.shoppinglist.ui.robot.overviewScreen
import md.vnastasi.shoppinglist.ui.rule.createDatabaseRule
import md.vnastasi.shoppinglist.ui.rule.createKoinTestModuleRule
import md.vnastasi.shoppinglist.ui.rule.disableAnimationsRule
import md.vnastasi.shoppinglist.ui.rule.retryOnFailureRule
import md.vnastasi.shoppinglist.ui.support.UiTestDispatcherProvider
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

class CompleteShoppingListFlowTest {

    private val composeRule = createAndroidComposeRule<MainActivity>()

    private val koinTestModeRule = createKoinTestModuleRule {
        single<DispatchersProvider> { UiTestDispatcherProvider() }
    }

    private val databaseRule = createDatabaseRule(
        setUp = {
            shoppingListDao().create(ShoppingList(1L, "Groceries"))
            shoppingListDao().create(ShoppingList(2L, "Gardening"))
            shoppingItemDao().create(ShoppingItem(1L, "Bread", false, 1L))
            shoppingItemDao().create(ShoppingItem(2L, "Milk", false, 1L))
            shoppingItemDao().create(ShoppingItem(3L, "Butter", false, 1L))
            shoppingItemDao().create(ShoppingItem(4L, "Apple juice", false, 1L))
            shoppingItemDao().create(ShoppingItem(5L, "Goat cheese", false, 1L))
            shoppingItemDao().create(ShoppingItem(6L, "Rake", false, 2L))
            shoppingItemDao().create(ShoppingItem(7L, "Soil", false, 2L))
        }
    )

    @get:Rule
    val ruleChain: TestRule = RuleChain
        .outerRule(composeRule)
        .around(retryOnFailureRule(maxAttempts = 3))
        .around(databaseRule)
        .around(koinTestModeRule)
        .around(disableAnimationsRule())

    @Test
    fun completeShoppingList() {
        overviewScreen(composeRule) {
            hasNoEmptyOverviewMessage()
            hasShoppingListCard("Groceries", 5, 0)
            hasShoppingListCard("Gardening", 2, 0)
            clickOnShoppingListCard("Groceries")
        }

        listDetailsScreen(composeRule) {
            hasToolbarName("Groceries")
            hasUncheckedItem("Bread")
            hasUncheckedItem("Milk")
            hasUncheckedItem("Butter")
            hasUncheckedItem("Apple juice")
            hasUncheckedItem("Goat cheese")
            clickOnItem("Bread")
            clickOnItem("Milk")
            clickOnItem("Butter")
            clickOnItem("Apple juice")
            clickOnItem("Goat cheese")
            hasCheckedItem("Bread")
            hasCheckedItem("Milk")
            hasCheckedItem("Butter")
            hasCheckedItem("Apple juice")
            hasCheckedItem("Goat cheese")
            navigateBack()
        }

        overviewScreen(composeRule) {
            hasShoppingListCard("Groceries", 5, 5)
            hasShoppingListCard("Gardening", 2, 0)
            clickOnShoppingListCard("Gardening")
        }

        listDetailsScreen(composeRule) {
            hasToolbarName("Gardening")
            hasUncheckedItem("Rake")
            hasUncheckedItem("Soil")
            clickOnItem("Rake")
            clickOnItem("Soil")
            navigateBack()
        }

        overviewScreen(composeRule) {
            hasShoppingListCard("Groceries", 5, 5)
            hasShoppingListCard("Gardening", 2, 2)
        }
    }
}
