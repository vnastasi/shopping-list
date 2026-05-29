package md.vnastasi.shoppinglist.ui

import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.result.rememberResultEventBusNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import md.vnastasi.shoppinglist.screen.additems.nav.addItems
import md.vnastasi.shoppinglist.screen.listdetails.nav.listDetails
import md.vnastasi.shoppinglist.screen.managelist.nav.manageList
import md.vnastasi.shoppinglist.screen.overview.nav.Overview
import md.vnastasi.shoppinglist.screen.overview.nav.overview
import md.vnastasi.shoppinglist.screen.shared.animation.slideInFromLeft
import md.vnastasi.shoppinglist.screen.shared.animation.slideInFromRight
import md.vnastasi.shoppinglist.screen.shared.animation.slideOutToLeft
import md.vnastasi.shoppinglist.screen.shared.animation.slideOutToRight
import md.vnastasi.shoppinglist.screen.shared.nav.scene.rememberBottomSheetSceneStrategy
import md.vnastasi.shoppinglist.screen.shared.nav.scene.rememberDialogWhenLargeSceneStrategy
import md.vnastasi.shoppinglist.screen.shared.nav.scene.rememberListDetailSceneStrategy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ApplicationScreenContainer() {

    val navBackStack = rememberNavBackStack(Overview)

    NavDisplay(
        backStack = navBackStack,
        sceneStrategies = listOf(
            rememberListDetailSceneStrategy(),
            rememberBottomSheetSceneStrategy(),
            rememberDialogWhenLargeSceneStrategy()
        ),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberResultEventBusNavEntryDecorator()
        ),
        transitionSpec = {
            slideInFromRight() togetherWith slideOutToLeft()
        },
        popTransitionSpec = {
            slideInFromLeft() togetherWith slideOutToRight()
        },
        predictivePopTransitionSpec = {
            slideInFromLeft() togetherWith slideOutToRight()
        },
        onBack = {
            navBackStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            overview(navBackStack)
            manageList(navBackStack)
            listDetails(navBackStack)
            addItems(navBackStack)
        }
    )
}
