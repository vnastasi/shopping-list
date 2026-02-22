package md.vnastasi.shoppinglist.ui.robot

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.rules.ActivityScenarioRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.NEW_SHOPPING_LIST_FAB
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.SHOPPING_LISTS_ITEM
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.SHOPPING_LISTS_ITEM_DELETE
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.SHOPPING_LISTS_ITEM_EDIT
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.SHOPPING_LISTS_LIST

private const val DEFAULT_TIMEOUT = 5_000L

@RobotDslMarker
context(composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>)
fun overviewScreen(block: OverviewScreenRobot.() -> Unit = { }) = OverviewScreenRobot(composeTestRule).apply(block)

@OptIn(ExperimentalTestApi::class)
class OverviewScreenRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    private val resources = composeTestRule.activity.resources

    fun hasEmptyOverviewMessage() {
        val matcher = hasText(resources.getString(R.string.overview_empty_list))
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).assertIsDisplayed()
    }

    fun hasNoEmptyOverviewMessage() {
        val matcher = hasText(resources.getString(R.string.overview_empty_list))
        composeTestRule.waitUntilDoesNotExist(matcher)
        composeTestRule.onNode(matcher).assertIsNotDisplayed()
    }

    fun clickOnNewShoppingListFab() {
        val matcher = hasTestTag(NEW_SHOPPING_LIST_FAB)
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).performClick()
    }

    fun hasShoppingListCard(name: String, totalItems: Int, completedItems: Int) {
        val listMatcher = hasTestTag(SHOPPING_LISTS_LIST)
        val itemMatcher = hasTestTag(SHOPPING_LISTS_ITEM) and hasAnyDescendant(hasText(name)) and hasAnyDescendant(hasText("$completedItems / $totalItems"))
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.waitUntilAtLeastOneExists(itemMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher)
            .performScrollToNode(itemMatcher)
            .assertIsDisplayed()
    }

    fun hasNoShoppingListCard(name: String) {
        val listMatcher = hasTestTag(SHOPPING_LISTS_LIST)
        val itemMatcher = hasTestTag(SHOPPING_LISTS_ITEM) and hasAnyDescendant(hasText(name))
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.waitUntilDoesNotExist(itemMatcher)
    }

    fun clickOnShoppingListCard(name: String) {
        val listMatcher = hasTestTag(SHOPPING_LISTS_LIST)
        val itemMatcher = hasTestTag(SHOPPING_LISTS_ITEM) and hasAnyDescendant(hasText(name))
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.waitUntilAtLeastOneExists(itemMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher).performScrollToNode(itemMatcher)
        composeTestRule.onNode(itemMatcher).performClick()
    }

    fun moveShoppingListCardUp(name: String) {
        val listMatcher = hasTestTag(SHOPPING_LISTS_LIST)
        val listItemMatcher = hasTestTag(SHOPPING_LISTS_ITEM) and hasAnyDescendant(hasText(name))
        val dragHandleMatcher = hasContentDescription(resources.getString(R.string.overview_item_drag_handle_btn_acc)) and hasAnyAncestor(listItemMatcher)
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.waitUntilAtLeastOneExists(dragHandleMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher).performScrollToNode(dragHandleMatcher)
        composeTestRule.onNode(dragHandleMatcher).performTouchInput {
            down(center)
            advanceEventTime(500L)
            moveBy(Offset(x = 0.0f, y = -100.dp.toPx()))
            advanceEventTime(2000L)
            up()
        }
    }

    fun moveShoppingListCardDown(name: String) {
        val listMatcher = hasTestTag(SHOPPING_LISTS_LIST)
        val listItemMatcher = hasTestTag(SHOPPING_LISTS_ITEM) and hasAnyDescendant(hasText(name))
        val dragHandleMatcher = hasContentDescription(resources.getString(R.string.overview_item_drag_handle_btn_acc)) and hasAnyAncestor(listItemMatcher)
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.waitUntilAtLeastOneExists(dragHandleMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher).performScrollToNode(dragHandleMatcher)
        composeTestRule.onNode(dragHandleMatcher).performTouchInput {
            down(center)
            advanceEventTime(500L)
            moveBy(Offset(x = 0.0f, y = 100.dp.toPx()))
            advanceEventTime(2000L)
            up()
        }
    }

    fun swipeShoppingListCard(name: String) {
        val listMatcher = hasTestTag(SHOPPING_LISTS_LIST)
        val itemMatcher = hasTestTag(SHOPPING_LISTS_ITEM) and hasAnyDescendant(hasText(name))
        composeTestRule.waitUntilAtLeastOneExists(listMatcher, DEFAULT_TIMEOUT)
        composeTestRule.waitUntilAtLeastOneExists(itemMatcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(listMatcher).performScrollToNode(itemMatcher)
        composeTestRule.onNode(itemMatcher).performTouchInput {
            down(center)
            advanceEventTime(500L)
            moveBy(Offset(x = -100.dp.toPx(), y = 0.0f))
            advanceEventTime(500L)
            up()
        }
    }

    fun clickOnEditShoppingList() {
        val matcher = hasTestTag(SHOPPING_LISTS_ITEM_EDIT)
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).performClick()
    }

    fun clickOnDeleteShoppingList() {
        val matcher = hasTestTag(SHOPPING_LISTS_ITEM_DELETE)
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).performClick()
    }
}
