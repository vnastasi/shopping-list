package md.vnastasi.shoppinglist.ui.robot

import android.content.res.Resources
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.NEW_SHOPPING_LIST_TEXT_FIELD

private const val DEFAULT_TIMEOUT = 5_000L

@RobotDslMarker
fun shoppingListForm(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    block: ShoppingListFormRobot.() -> Unit = {}
) = ShoppingListFormRobot(composeTestRule).apply(block)

@OptIn(ExperimentalTestApi::class)
class ShoppingListFormRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    private val resources: Resources = composeTestRule.activity.resources

    fun typeShoppingListName(value: String) {
        val matcher = hasTestTag(NEW_SHOPPING_LIST_TEXT_FIELD)
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).performTextInput(value)
    }

    fun clickOnSaveButton() {
        val matcher = hasText(resources.getString(R.string.list_form_btn_save))
        composeTestRule.waitUntilAtLeastOneExists(matcher, DEFAULT_TIMEOUT)
        composeTestRule.onNode(matcher).performClick()
    }
}
