package md.vnastasi.shoppinglist.ui.flow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.db.ShoppingListDatabase
import md.vnastasi.shoppinglist.ui.robot.addItemsScreen
import md.vnastasi.shoppinglist.ui.robot.listDetailsScreen
import md.vnastasi.shoppinglist.ui.robot.overviewScreen
import md.vnastasi.shoppinglist.ui.robot.shoppingListForm
import md.vnastasi.shoppinglist.ui.rule.disableAnimationsRule
import md.vnastasi.shoppinglist.ui.rule.onSetUp
import md.vnastasi.shoppinglist.ui.rule.onTearDown
import md.vnastasi.shoppinglist.ui.rule.retryOnFailureRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import javax.inject.Inject

@HiltAndroidTest
class CreateShoppingListFlowTest {

    private val hiltAndroidRule = HiltAndroidRule(this)

    private val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val ruleChain: TestRule = RuleChain
        .outerRule(hiltAndroidRule)
        .around(composeRule)
        .around(retryOnFailureRule(maxAttempts = 3))
        .around(disableAnimationsRule())

    @Inject
    lateinit var shoppingListDatabase: ShoppingListDatabase

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        shoppingListDatabase.onSetUp()
    }

    @After
    fun tearDown() {
        shoppingListDatabase.onTearDown()
    }

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
            typeSearchQuery("Hammer")
            acceptValueFromKeyboard()
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
            hasUncheckedItem("Hammer")
            deleteItem("Hammer")
            hasNoItem("Hammer")
            navigateBack()
        }

        overviewScreen(composeRule) {
            hasShoppingListCard("Test new shopping list", 3, 0)
        }
    }
}
