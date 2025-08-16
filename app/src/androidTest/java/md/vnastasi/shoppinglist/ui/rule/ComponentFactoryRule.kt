package md.vnastasi.shoppinglist.ui.rule

import androidx.test.ext.junit.rules.AppComponentFactoryRule
import org.junit.rules.TestRule
import org.koin.core.module.Module
import org.koin.dsl.module

fun createComponentFactoryRule(block: Module.() -> Unit = { }): TestRule =
    AppComponentFactoryRule(OverridingComponentFactory(module(createdAtStart = false, block)))

private class OverridingComponentFactory(
    private val overridingModule: Module
) : ComponentFactory() {

    private val realModules: List<Module>
        get() = super.modules()

    override fun modules(): List<Module> = realModules + overridingModule
}
