package md.vnastasi.shoppinglist.ui.rule

import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun createKoinTestModule(block: Module.() -> Unit): TestRule = KoinTestModuleRule(block)

private class KoinTestModuleRule(private val block: Module.() -> Unit) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        loadKoinModules(module(false, block))
    }
}
