package md.vnastasi.shoppinglist.ui.robot

import android.content.res.Resources
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.managelist.ui.TestTags.MANAGE_LIST_TEXT_FIELD

private const val DEFAULT_TIMEOUT = 5_000L

@RobotDslMarker
context(composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>)
fun manageShoppingListSheet(block: ManageShoppingListSheetRobot.() -> Unit = {}) = ManageShoppingListSheetRobot(composeTestRule).apply(block)

@OptIn(ExperimentalTestApi::class)
class ManageShoppingListSheetRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    private val resources: Resources
        get() = composeTestRule.activity.resources

    fun hasPrefilledShoppingListName(value: String) {
        val matcher = hasTestTag(MANAGE_LIST_TEXT_FIELD)
        composeTestRule.waitForIdle()
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).assert(hasText(value))
    }

    fun typeShoppingListName(value: String) {
        val matcher = hasTestTag(MANAGE_LIST_TEXT_FIELD)
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).performTextClearance()
        composeTestRule.onNode(matcher).performTextInput(value)
    }

    fun clickOnSaveButton() {
        val matcher = hasText(resources.getString(R.string.list_form_btn_save))
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).performClick()
    }
}
