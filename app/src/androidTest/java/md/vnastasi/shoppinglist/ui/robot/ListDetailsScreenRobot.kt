package md.vnastasi.shoppinglist.ui.robot

import android.content.res.Resources
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.rules.ActivityScenarioRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.LIST_DETAILS_TOOLBAR
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.LIST_ITEM
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.LIST_ITEM_CHECKBOX

context(AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>)
@RobotDslMarker
fun listDetailsScreen(block: ListDetailsScreenRobot.() -> Unit = {}) = with(activity.resources) { ListDetailsScreenRobot().apply(block) }

context(AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>, Resources)
class ListDetailsScreenRobot {

    fun navigateBack() {
        activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
    }

    fun hasToolbarName(value: String) {
        val matcher = hasTestTag(LIST_DETAILS_TOOLBAR) and hasAnyDescendant(hasTestTag(value))
        onNode(matcher).isDisplayed()
    }

    fun hasEmptyShoppingListMessage() {
        val matcher = hasText(getString(R.string.list_details_empty))
        onNode(matcher).isDisplayed()
    }

    fun hasNoEmptyShoppingListMessage() {
        val matcher = hasText(getString(R.string.list_details_empty))
        onNode(matcher).isNotDisplayed()
    }

    fun clickOnAddItemsFab() {
        val matcher = hasTestTag(TestTags.ADD_LIST_ITEMS_FAB)
        onNode(matcher).performClick()
    }

    fun hasCheckedItem(name: String) {
        val matcher = hasTestTag(LIST_ITEM) and hasAnyDescendant(hasText(name)) and hasAnyDescendant(hasTestTag(LIST_ITEM_CHECKBOX) and isOn())
        onNode(matcher).performScrollTo().isDisplayed()
    }

    fun hasUncheckedItem(name: String) {
        val matcher = hasTestTag(LIST_ITEM) and hasAnyDescendant(hasText(name)) and hasAnyDescendant(hasTestTag(LIST_ITEM_CHECKBOX) and isOff())
        onNode(matcher).performScrollTo().isDisplayed()
    }

    fun clickOnItem(name: String) {
        val matcher = hasTestTag(LIST_ITEM) and hasAnyDescendant(hasText(name))
        onNode(matcher).performScrollTo().performClick()
    }
}
