package md.vnastasi.shoppinglist.ui.robot

import android.content.res.Resources
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SEARCH_BAR
import md.vnastasi.shoppinglist.screen.additems.ui.TestTags.SUGGESTION_ITEM

context(AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>)
@RobotDslMarker
fun addItemsScreen(block: AddItemsScreenRobot.() -> Unit = {}) = with(activity.resources) { AddItemsScreenRobot().apply(block) }

context(AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>, Resources)
class AddItemsScreenRobot {

    fun navigateBack() {
        activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
    }

    fun hasEmptySearchbar() {
        val matcher = hasTestTag(SEARCH_BAR) and hasAnyDescendant(hasText(getString(R.string.add_items_search_title)))
        onNode(matcher).isDisplayed()
    }

    fun typeSearchQuery(value: String) {
        val matcher = hasTestTag(SEARCH_BAR)
        onNode(matcher).performTextInput(value)
    }

    fun clearSearchbar() {
        val matcher = hasTestTag(SEARCH_BAR)
        onNode(matcher).performTextClearance()
    }

    fun acceptValueFromKeyboard() {
        val matcher = hasTestTag(SEARCH_BAR)
        onNode(matcher).performImeAction()
    }

    fun hasSuggestionItem(name: String) {
        val matcher = hasTestTag(SUGGESTION_ITEM) and hasAnyDescendant(hasText(name))
        onNode(matcher).performScrollTo().isDisplayed()
    }

    fun clickOnSuggestionItem(name: String) {
        val matcher = hasTestTag(SUGGESTION_ITEM) and hasAnyDescendant(hasText(name))
        onNode(matcher).performScrollTo().performClick()
    }
}