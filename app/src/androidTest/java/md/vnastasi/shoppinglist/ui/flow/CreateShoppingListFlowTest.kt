package md.vnastasi.shoppinglist.ui.flow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.support.async.DispatchersProvider
import md.vnastasi.shoppinglist.ui.robot.addItemsScreen
import md.vnastasi.shoppinglist.ui.robot.listDetailsScreen
import md.vnastasi.shoppinglist.ui.robot.overviewScreen
import md.vnastasi.shoppinglist.ui.robot.shoppingListForm
import md.vnastasi.shoppinglist.ui.rule.createDatabaseRule
import md.vnastasi.shoppinglist.ui.rule.createKoinTestModuleRule
import md.vnastasi.shoppinglist.ui.rule.disableAnimationsRule
import md.vnastasi.shoppinglist.ui.rule.retryOnFailureRule
import md.vnastasi.shoppinglist.ui.support.UiTestDispatcherProvider
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

class CreateShoppingListFlowTest {

    private val composeRule = createAndroidComposeRule<MainActivity>()

    private val koinTestModeRule = createKoinTestModuleRule {
        single<DispatchersProvider> { UiTestDispatcherProvider() }
    }

    @get:Rule
    val ruleChain: TestRule = RuleChain
        .outerRule(composeRule)
        .around(retryOnFailureRule(maxAttempts = 3))
        .around(createDatabaseRule())
        .around(koinTestModeRule)
        .around(disableAnimationsRule())

    @Test
    fun createNewShoppingList() {
        overviewScreen(composeRule) {
            hasEmptyOverviewMessage()
            clickOnNewShoppingListFab()
        }

        shoppingListForm(composeRule) {
            typeShoppingListName("Test new shopping list")
            clickOnSaveButton()
        }

        overviewScreen(composeRule) {
            hasNoEmptyOverviewMessage()
            hasShoppingListCard("Test new shopping list", 0, 0)
            clickOnShoppingListCard("Test new shopping list")
        }

        listDetailsScreen(composeRule) {
            hasToolbarName("Test new shopping list")
            hasEmptyShoppingListMessage()
            clickOnAddItemsFab()
        }

        addItemsScreen(composeRule) {
            hasEmptySearchbar()
            typeSearchQuery("Milk")
            clickOnSuggestionItem("Milk")
            clearSearchbar()
            typeSearchQuery("Bread")
            acceptValueFromKeyboard()
            typeSearchQuery("Coconut oil")
            acceptValueFromKeyboard()
            navigateBack()
        }

        listDetailsScreen(composeRule) {
            hasNoEmptyShoppingListMessage()
            hasUncheckedItem("Coconut oil")
            hasUncheckedItem("Bread")
            hasUncheckedItem("Milk")
            navigateBack()
        }

        overviewScreen(composeRule) {
            hasShoppingListCard("Test new shopping list", 3, 0)
        }
    }
}
