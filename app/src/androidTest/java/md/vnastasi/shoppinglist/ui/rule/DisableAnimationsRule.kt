package md.vnastasi.shoppinglist.ui.rule

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

fun disableAnimationsRule(): TestRule = DisableAnimationsRule()

private class DisableAnimationsRule : TestWatcher() {

    private val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    override fun starting(description: Description?) {
        setAnimationScale(0)
    }

    override fun finished(description: Description?) {
        setAnimationScale(1)
    }

    private fun setAnimationScale(scale: Int) {
        uiDevice.executeShellCommand("settings put global window_animation_scale $scale")
        uiDevice.executeShellCommand("settings put global transition_animation_scale $scale")
        uiDevice.executeShellCommand("settings put global animator_duration_scale $scale")
    }
}
