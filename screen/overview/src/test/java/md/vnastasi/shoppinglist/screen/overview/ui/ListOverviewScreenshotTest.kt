package md.vnastasi.shoppinglist.screen.overview.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import md.vnastasi.shoppinglist.screen.overview.nav.ListOverviewScreenNavigator
import md.vnastasi.shoppinglist.screen.overview.vm.ListOverviewViewModelSpec
import md.vnastasi.shoppinglist.screen.overview.model.UiEvent
import md.vnastasi.shoppinglist.screen.overview.model.ViewState
import org.junit.Rule
import org.junit.Test

class ListOverviewScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        showSystemUi = false,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        validateAccessibility = false,
        maxPercentDifference = 1.0
    )

    @Test
    fun emptyScreen() {
        val viewState = ViewState(
            shoppingLists = persistentListOf(),
            navigationTarget = null,
            toastMessage = null
        )
        paparazzi.snapshot("list_overview_empty") {
            ListOverviewScreen(
                viewModel = StubListOverviewViewModel(viewState),
                navigator = StubListOverviewScreenNavigator()
            )
        }
    }

    private class StubListOverviewScreenNavigator : ListOverviewScreenNavigator {

        override fun toListDetails(shoppingListId: Long) = Unit
    }

    private class StubListOverviewViewModel(private val viewState: ViewState) : ListOverviewViewModelSpec {

        override val screenState: StateFlow<ViewState>
            get() = MutableStateFlow(viewState)

        override fun onUiEvent(uiEvent: UiEvent) = Unit
    }
}
