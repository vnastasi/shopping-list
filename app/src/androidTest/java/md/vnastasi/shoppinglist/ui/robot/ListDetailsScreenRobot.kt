package md.vnastasi.shoppinglist.ui.robot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.rules.ActivityScenarioRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.LIST_DETAILS_TOOLBAR
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.LIST_ITEM_CHECKBOX
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.SHOPPING_ITEMS_ITEM
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.SHOPPING_ITEMS_LIST

@RobotDslMarker
fun listDetailsScreen(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    block: ListDetailsScreenRobot.() -> Unit = {}
) = ListDetailsScreenRobot(composeTestRule).apply(block)

class ListDetailsScreenRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    private val resources = composeTestRule.activity.resources

    fun navigateBack() {
        composeTestRule.activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
    }

    fun hasToolbarName(value: String) {
        val matcher = hasTestTag(LIST_DETAILS_TOOLBAR) and hasAnyDescendant(hasTestTag(value))
        composeTestRule.onNode(matcher).isDisplayed()
    }

    fun hasEmptyShoppingListMessage() {
        val matcher = hasText(resources.getString(R.string.list_details_empty))
        composeTestRule.onNode(matcher).isDisplayed()
    }

    fun hasNoEmptyShoppingListMessage() {
        val matcher = hasText(resources.getString(R.string.list_details_empty))
        composeTestRule.onNode(matcher).isNotDisplayed()
    }

    fun clickOnAddItemsFab() {
        val matcher = hasTestTag(TestTags.ADD_SHOPPING_LIST_ITEMS_FAB)
        composeTestRule.onNode(matcher).performClick()
    }

    fun hasCheckedItem(name: String) {
        val itemMatcher = hasTestTag(SHOPPING_ITEMS_ITEM) and hasAnyDescendant(hasText(name)) and hasAnyDescendant(hasTestTag(LIST_ITEM_CHECKBOX) and isOn())
        composeTestRule.onNode(hasTestTag(SHOPPING_ITEMS_LIST)).performScrollToNode(itemMatcher)
        composeTestRule.onNode(itemMatcher).assertIsDisplayed()
    }

    fun hasUncheckedItem(name: String) {
        val itemMatcher = hasTestTag(SHOPPING_ITEMS_ITEM) and hasAnyDescendant(hasText(name)) and hasAnyDescendant(hasTestTag(LIST_ITEM_CHECKBOX) and isOff())
        composeTestRule.onNode(hasTestTag(SHOPPING_ITEMS_LIST)).performScrollToNode(itemMatcher)
        composeTestRule.onNode(itemMatcher).assertIsDisplayed()
    }

    fun clickOnItem(name: String) {
        val itemMatcher = hasTestTag(SHOPPING_ITEMS_ITEM) and hasAnyDescendant(hasText(name))
        composeTestRule.onNode(hasTestTag(SHOPPING_ITEMS_LIST)).performScrollToNode(itemMatcher)
        composeTestRule.onNode(itemMatcher).performClick()
    }
}
