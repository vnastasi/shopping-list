package md.vnastasi.shoppinglist.ui.robot

import android.content.res.Resources
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

context(AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>)
@RobotDslMarker
fun addItemsScreen(block: AddItemsScreenRobot.() -> Unit = {}) = with(activity.resources) { AddItemsScreenRobot().apply(block) }

context(AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>, Resources)
class AddItemsScreenRobot {

    fun navigateBack() {
        onNode(hasTestTag(BACK_BUTTON)).performClick()
    }

    fun hasEmptySearchbar() {
        val matcher = hasTestTag(SEARCH_BAR) and hasAnyDescendant(hasText(getString(R.string.add_items_search_title)))
        onNode(matcher, useUnmergedTree = true).assertIsDisplayed()
    }

    fun typeSearchQuery(value: String) {
        val matcher = hasTestTag(SEARCH_BAR)
        onNode(matcher, useUnmergedTree = true).performTextInput(value)
    }

    fun clearSearchbar() {
        val matcher = hasTestTag(SEARCH_BAR)
        onNode(matcher, useUnmergedTree = true).performTextClearance()
    }

    fun acceptValueFromKeyboard() {
        val matcher = hasTestTag(SEARCH_BAR)
        onNode(matcher, useUnmergedTree = true).performImeAction()
    }

    fun hasSuggestionItem(name: String) {
        val itemMatcher = hasTestTag(SUGGESTIONS_ITEM) and hasAnyDescendant(hasText(name))
        onNode(hasTestTag(SUGGESTIONS_LIST)).performScrollToNode(itemMatcher)
        onNode(itemMatcher).assertIsDisplayed()
    }

    fun hasNoSuggestionItem(name: String) {
        val itemMatcher = hasTestTag(SUGGESTIONS_ITEM) and hasAnyDescendant(hasText(name))
        onNode(hasTestTag(SUGGESTIONS_LIST))
            .onChildren()
            .filter(itemMatcher)
            .assertCountEquals(0)
    }

    fun clickOnSuggestionItem(name: String) {
        val itemMatcher = hasTestTag(SUGGESTIONS_ITEM) and hasAnyDescendant(hasText(name))
        onNode(hasTestTag(SUGGESTIONS_LIST)).performScrollToNode(itemMatcher)
        onNode(itemMatcher).performClick()
    }

    fun deleteSuggestionItem(name: String) {
        val itemMatcher = hasTestTag(SUGGESTIONS_ITEM) and hasAnyDescendant(hasText(name))
        val deleteButtonMatcher = hasTestTag(SUGGESTIONS_ITEM_DELETE_BUTTON) and hasAnyAncestor(itemMatcher)
        onNode(hasTestTag(SUGGESTIONS_LIST)).performScrollToNode(itemMatcher)
        onNode(deleteButtonMatcher).performClick()
    }
}