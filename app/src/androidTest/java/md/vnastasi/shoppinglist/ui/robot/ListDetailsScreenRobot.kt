package md.vnastasi.shoppinglist.ui.robot

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.rules.ActivityScenarioRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.LIST_DETAILS_TOOLBAR
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.LIST_ITEM_CHECKBOX
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.SHOPPING_ITEMS_ITEM
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.SHOPPING_ITEMS_ITEM_DELETE_BUTTON
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.SHOPPING_ITEMS_LIST

private const val DEFAULT_TIMEOUT = 5_000L

@RobotDslMarker
context(composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>)
fun listDetailsScreen(block: ListDetailsScreenRobot.() -> Unit = {}) = ListDetailsScreenRobot(composeTestRule).apply(block)

@OptIn(ExperimentalTestApi::class)
class ListDetailsScreenRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    private val resources = composeTestRule.activity.resources

    fun navigateBack() {
        composeTestRule.activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
    }

    fun hasToolbarName(value: String) {
        val matcher = hasTestTag(LIST_DETAILS_TOOLBAR) and hasAnyDescendant(hasText(value))
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).isDisplayed()
    }

    fun hasEmptyShoppingListMessage() {
        val matcher = hasText(resources.getString(R.string.list_details_empty))
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).isDisplayed()
    }

    fun hasNoEmptyShoppingListMessage() {
        val matcher = hasText(resources.getString(R.string.list_details_empty))
        composeTestRule.onNode(matcher).isNotDisplayed()
    }

    fun clickOnAddItemsFab() {
        val matcher = hasTestTag(TestTags.ADD_SHOPPING_LIST_ITEMS_FAB)
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).performClick()
    }

    fun hasNoItem(name: String) {
        val listMatcher = hasTestTag(SHOPPING_ITEMS_LIST)
        val itemMatcher = hasTestTag(SHOPPING_ITEMS_ITEM) and hasAnyDescendant(hasText(name))
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher).onChildren().filter(itemMatcher).assertCountEquals(0)
    }

    fun hasCheckedItem(name: String) {
        val listMatcher = hasTestTag(SHOPPING_ITEMS_LIST)
        val itemMatcher = hasTestTag(SHOPPING_ITEMS_ITEM) and hasAnyDescendant(hasText(name)) and hasAnyDescendant(hasTestTag(LIST_ITEM_CHECKBOX) and isOn())
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.waitUntilAtLeastOneExists(itemMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher).performScrollToNode(itemMatcher)
        composeTestRule.onNode(itemMatcher).assertIsDisplayed()
    }

    fun hasUncheckedItem(name: String) {
        val listMatcher = hasTestTag(SHOPPING_ITEMS_LIST)
        val itemMatcher = hasTestTag(SHOPPING_ITEMS_ITEM) and hasAnyDescendant(hasText(name)) and hasAnyDescendant(hasTestTag(LIST_ITEM_CHECKBOX) and isOff())
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.waitUntilAtLeastOneExists(itemMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher).performScrollToNode(itemMatcher)
        composeTestRule.onNode(itemMatcher).assertIsDisplayed()
    }

    fun clickOnItem(name: String) {
        val listMatcher = hasTestTag(SHOPPING_ITEMS_LIST)
        val itemMatcher = hasTestTag(SHOPPING_ITEMS_ITEM) and hasAnyDescendant(hasText(name))
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.waitUntilAtLeastOneExists(itemMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher).performScrollToNode(itemMatcher)
        composeTestRule.onNode(itemMatcher).performClick()
    }

    fun deleteItem(name: String) {
        val listMatcher = hasTestTag(SHOPPING_ITEMS_LIST)
        val itemMatcher = hasTestTag(SHOPPING_ITEMS_ITEM) and hasAnyDescendant(hasText(name))
        val deleteButtonMatcher = hasTestTag(SHOPPING_ITEMS_ITEM_DELETE_BUTTON) and hasAnyAncestor(itemMatcher)
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.waitUntilAtLeastOneExists(itemMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher).performScrollToNode(itemMatcher)
        composeTestRule.onNode(deleteButtonMatcher).performClick()
    }
}
