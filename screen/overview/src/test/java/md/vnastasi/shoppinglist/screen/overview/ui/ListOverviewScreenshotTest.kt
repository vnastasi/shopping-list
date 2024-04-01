package md.vnastasi.shoppinglist.screen.overview.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.NightMode
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.TestData.createShoppingList
import md.vnastasi.shoppinglist.screen.overview.model.NavigationTarget
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import md.vnastasi.shoppinglist.support.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class ListOverviewScreenshotTest(
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
                ListOverviewScreen(
                    viewModel = StubListOverviewViewModel(viewState),
                    navigator = StubListOverviewScreenNavigator()
                )
            }
        }
    }

    companion object {

        @JvmStatic
        @Parameters
        fun parameters(): List<Array<Any>> = listOf(
            arguments {
                screenshotName = "empty_list_dark"
                viewState = ViewState(
                    shoppingLists = persistentListOf()
                )
                nightMode = NightMode.NIGHT
            },
            arguments {
                screenshotName = "empty_list_light"
                viewState = ViewState(
                    shoppingLists = persistentListOf()
                )
                nightMode = NightMode.NOTNIGHT
            },
            arguments {
                screenshotName = "non_empty_list_dark"
                viewState = ViewState(
                    shoppingLists = persistentListOf(
                        createShoppingList {
                            id = 1L
                            name = "Test list 1"
                        },
                        createShoppingList {
                            id = 2L
                            name = "Test list 2"
                        }
                    )
                )
                nightMode = NightMode.NIGHT
            },
            arguments {
                screenshotName = "non_empty_list_light"
                viewState = ViewState(
                    shoppingLists = persistentListOf(
                        createShoppingList {
                            id = 1L
                            name = "Test list 1"
                        },
                        createShoppingList {
                            id = 2L
                            name = "Test list 2"
                        }
                    )
                )
                nightMode = NightMode.NOTNIGHT
            },
            arguments {
                screenshotName = "form_dark"
                viewState = ViewState(
                    shoppingLists = persistentListOf(),
                    navigationTarget = NavigationTarget.ShoppingListForm
                )
                nightMode = NightMode.NIGHT
            },
            arguments {
                screenshotName = "form_light"
                viewState = ViewState(
                    shoppingLists = persistentListOf(),
                    navigationTarget = NavigationTarget.ShoppingListForm
                )
                nightMode = NightMode.NOTNIGHT
            }
        )
    }
}

private fun arguments(block: TestArguments.() -> Unit) = TestArguments().apply(block).toArray()

private class TestArguments {

    lateinit var screenshotName: String
    lateinit var viewState: ViewState
    lateinit var nightMode: NightMode

    fun toArray(): Array<Any> = arrayOf(screenshotName, viewState, nightMode)
}
