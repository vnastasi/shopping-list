package md.vnastasi.shoppinglist.ui.robot

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.BACK_BUTTON
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SEARCH_BAR
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SUGGESTIONS_ITEM
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SUGGESTIONS_ITEM_DELETE_BUTTON
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SUGGESTIONS_LIST

private const val DEFAULT_TIMEOUT = 5_000L

@RobotDslMarker
fun addItemsScreen(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    block: AddItemsScreenRobot.() -> Unit = {}
) = AddItemsScreenRobot(composeTestRule).apply(block)

@OptIn(ExperimentalTestApi::class)
class AddItemsScreenRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    private val resources = composeTestRule.activity.resources

    fun navigateBack() {
        val matcher = hasTestTag(BACK_BUTTON)
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).performClick()
    }

    fun hasEmptySearchbar() {
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(SEARCH_BAR), DEFAULT_TIMEOUT)
        val matcher = hasTestTag(SEARCH_BAR) and hasAnyDescendant(hasText(resources.getString(R.string.add_items_search_title)))
        composeTestRule.onNode(matcher, useUnmergedTree = true).assertIsDisplayed()
    }

    fun typeSearchQuery(value: String) {
        val matcher = hasTestTag(SEARCH_BAR)
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher, useUnmergedTree = true).performTextInput(value)
    }

    fun clearSearchbar() {
        val matcher = hasTestTag(SEARCH_BAR)
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher, useUnmergedTree = true).performTextClearance()
    }

    fun acceptValueFromKeyboard() {
        val matcher = hasTestTag(SEARCH_BAR)
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher, useUnmergedTree = true).performImeAction()
    }

    fun hasSuggestionItem(name: String) {
        val listMatcher = hasTestTag(SUGGESTIONS_LIST)
        val itemMatcher = hasTestTag(SUGGESTIONS_ITEM) and hasAnyDescendant(hasText(name))
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher).performScrollToNode(itemMatcher)
        composeTestRule.waitUntilAtLeastOneExists(itemMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(itemMatcher).assertIsDisplayed()
    }

    fun hasNoSuggestionItem(name: String) {
        val listMatcher = hasTestTag(SUGGESTIONS_LIST)
        val itemMatcher = hasTestTag(SUGGESTIONS_ITEM) and hasAnyDescendant(hasText(name))
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher)
            .onChildren()
            .filter(itemMatcher)
            .assertCountEquals(0)
    }

    fun clickOnSuggestionItem(name: String) {
        val listMatcher = hasTestTag(SUGGESTIONS_LIST)
        val itemMatcher = hasTestTag(SUGGESTIONS_ITEM) and hasAnyDescendant(hasText(name))
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher).performScrollToNode(itemMatcher)
        composeTestRule.waitUntilAtLeastOneExists(itemMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(itemMatcher).performClick()
    }

    fun deleteSuggestionItem(name: String) {
        val listMatcher = hasTestTag(SUGGESTIONS_LIST)
        val itemMatcher = hasTestTag(SUGGESTIONS_ITEM) and hasAnyDescendant(hasText(name))
        val deleteButtonMatcher = hasTestTag(SUGGESTIONS_ITEM_DELETE_BUTTON) and hasAnyAncestor(itemMatcher)
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher).performScrollToNode(itemMatcher)
        composeTestRule.waitUntilAtLeastOneExists(deleteButtonMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(deleteButtonMatcher).performClick()
    }
}