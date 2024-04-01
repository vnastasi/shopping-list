package md.vnastasi.shoppinglist.screen.listdetails.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.NightMode
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.TestData.createShoppingItem
import md.vnastasi.shoppinglist.screen.listdetails.model.ViewState
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class ListDetailsScreenshotTest(
    private val screenshotName: String,
    private val viewState: ViewState,
    private val nightMode: NightMode
) {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        showSystemUi = false,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        validateAccessibility = false,
        maxPercentDifference = 1.0
    )

    @Test
    fun screenshot() {
        paparazzi.snapshot(screenshotName) {
            AppTheme(useDarkTheme = nightMode == NightMode.NIGHT) {
                ListDetailsScreen(
                    viewModel = StubListDetailsViewModelSpec(viewState),
                    navigator = StubListDetailsScreenNavigator()
                )
            }
        }
    }

    companion object {

        @JvmStatic
        @Parameters
        fun parameters(): List<Array<Any>> = listOf(
            arguments {
                screenshotName = "list_details_empty_night"
                viewState = ViewState(
                    shoppingListId = 1L,
                    shoppingListName = "Test list",
                    listOfShoppingItems = persistentListOf()
                )
                nightMode = NightMode.NIGHT
            },
            arguments {
                screenshotName = "list_details_empty_light"
                viewState = ViewState(
                    shoppingListId = 1L,
                    shoppingListName = "Test list",
                    listOfShoppingItems = persistentListOf()
                )
                nightMode = NightMode.NOTNIGHT
            },
            arguments {
                screenshotName = "list_details_dark"
                viewState = ViewState(
                    shoppingListId = 1L,
                    shoppingListName = "Test list",
                    listOfShoppingItems = persistentListOf(
                        createShoppingItem {
                            id = 1L
                            name = "Item 1"
                            isChecked = false
                        },
                        createShoppingItem {
                            id = 2L
                            name = "Item 2"
                            isChecked = true
                        }
                    )
                )
                nightMode = NightMode.NIGHT
            },
            arguments {
                screenshotName = "list_details_light"
                viewState = ViewState(
                    shoppingListId = 1L,
                    shoppingListName = "Test list",
                    listOfShoppingItems = persistentListOf(
                        createShoppingItem {
                            id = 1L
                            name = "Item 1"
                            isChecked = false
                        },
                        createShoppingItem {
                            id = 2L
                            name = "Item 2"
                            isChecked = true
                        }
                    )
                )
                nightMode = NightMode.NOTNIGHT
            }
        )
    }
}

private class TestArguments {

    lateinit var screenshotName: String
    lateinit var viewState: ViewState
    lateinit var nightMode: NightMode

    fun toArray(): Array<Any> = arrayOf(screenshotName, viewState, nightMode)
}

private fun arguments(block: TestArguments.() -> Unit) = TestArguments().apply(block).toArray()
