package md.vnastasi.shoppinglist.ui.flow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.ui.robot.addItemsScreen
import md.vnastasi.shoppinglist.ui.robot.listDetailsScreen
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
class CreateShoppingListFlowTest {

    private val hiltAndroidRule = HiltAndroidRule(this)

    private val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val ruleChain: TestRule = RuleChain
        .outerRule(hiltAndroidRule)
        .around(databaseRule())
        .around(composeRule)
        .around(retryOnFailureRule(maxAttempts = 3))
        .around(disableAnimationsRule())

    @Test
    fun createNewShoppingList(): Unit = with(composeRule) {
        overviewScreen {
            hasEmptyOverviewMessage()
            clickOnNewShoppingListFab()
        }

        manageShoppingListSheet {
            typeShoppingListName("Test new shopping list")
            clickOnSaveButton()
        }

        overviewScreen {
            hasNoEmptyOverviewMessage()
            hasShoppingListCard("Test new shopping list", 0, 0)
            clickOnShoppingListCard("Test new shopping list")
        }

        listDetailsScreen {
            hasToolbarName("Test new shopping list")
            hasEmptyShoppingListMessage()
            clickOnAddItemsFab()
        }

        addItemsScreen {
            hasEmptySearchbar()
            typeSearchQuery("Milk")
            clickOnSuggestionItem("Milk")
            clearSearchbar()
            typeSearchQuery("Hammer")
            acceptValueFromKeyboard()
            typeSearchQuery("Bread")
            acceptValueFromKeyboard()
            typeSearchQuery("Coconut oil")
            acceptValueFromKeyboard()
            navigateBack()
        }

        listDetailsScreen {
            hasNoEmptyShoppingListMessage()
            hasUncheckedItem("Coconut oil")
            hasUncheckedItem("Bread")
            hasUncheckedItem("Milk")
            hasUncheckedItem("Hammer")
            deleteItem("Hammer")
            hasNoItem("Hammer")
            navigateBack()
        }

        overviewScreen {
            hasShoppingListCard("Test new shopping list", 3, 0)
        }
    }
}
