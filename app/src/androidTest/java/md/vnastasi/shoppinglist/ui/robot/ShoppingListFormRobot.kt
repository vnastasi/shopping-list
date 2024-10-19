package md.vnastasi.shoppinglist.ui.robot

import android.content.res.Resources
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.NEW_SHOPPING_LIST_TEXT_FIELD

@RobotDslMarker
fun shoppingListForm(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    block: ShoppingListFormRobot.() -> Unit = {}
) = ShoppingListFormRobot(composeTestRule).apply(block)

class ShoppingListFormRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    private val resources: Resources = composeTestRule.activity.resources

    fun typeShoppingListName(value: String) {
        composeTestRule.onNode(hasTestTag(NEW_SHOPPING_LIST_TEXT_FIELD)).performTextInput(value)
    }

    fun clickOnSaveButton() {
        composeTestRule.onNode(hasText(resources.getString(R.string.list_form_btn_save))).performClick()
    }
}
