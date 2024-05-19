package md.vnastasi.shoppinglist.ui.robot

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.NEW_SHOPPING_LIST_TEXT_FIELD

context(AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>)
@RobotDslMarker
fun shoppingListForm(block: ShoppingListFormRobot.() -> Unit = {}) = with(activity.resources) { ShoppingListFormRobot().apply(block) }

context(AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>, Resources)
class ShoppingListFormRobot {

    fun typeShoppingListName(value: String) {
        onNode(hasTestTag(NEW_SHOPPING_LIST_TEXT_FIELD)).performTextInput(value)
    }

    fun clickOnSaveButton() {
        onNode(hasText(getString(R.string.list_form_btn_save))).performClick()
    }
}
