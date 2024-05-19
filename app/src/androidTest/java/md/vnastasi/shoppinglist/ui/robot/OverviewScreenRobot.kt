package md.vnastasi.shoppinglist.ui.robot

import android.content.res.Resources
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.rules.ActivityScenarioRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.NEW_SHOPPING_LIST_FAB
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.SHOPPING_LIST_CARD

context(AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>)
@RobotDslMarker
fun overviewScreen(block: OverviewScreenRobot.() -> Unit = {}) = with(activity.resources) { OverviewScreenRobot().apply(block) }

context(AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>, Resources)
class OverviewScreenRobot {

    fun hasEmptyOverviewMessage() {
        val matcher = hasText(getString(R.string.overview_empty_list))
        onNode(matcher).isDisplayed()
    }

    fun hasNoEmptyOverviewMessage() {
        val matcher = hasText(getString(R.string.overview_empty_list))
        onNode(matcher).isNotDisplayed()
    }

    fun clickOnNewShoppingListFab() {
        val matcher = hasTestTag(NEW_SHOPPING_LIST_FAB)
        onNode(matcher).performClick()
    }

    fun hasShoppingListCard(name: String, totalItems: Int, completedItems: Int) {
        val matcher = hasTestTag(SHOPPING_LIST_CARD) and hasAnyDescendant(hasText(name)) and hasAnyDescendant(hasText("$completedItems / $totalItems"))
        onNode(matcher).performScrollTo().isDisplayed()
    }

    fun clickOnShoppingListCard(name: String) {
        val matcher = hasTestTag(SHOPPING_LIST_CARD) and hasAnyDescendant(hasText(name))
        onNode(matcher).performScrollTo().performClick()
    }
}
