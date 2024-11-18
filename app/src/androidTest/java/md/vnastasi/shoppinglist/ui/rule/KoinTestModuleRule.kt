package md.vnastasi.shoppinglist.ui.rule

import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.definition.IndexKey
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.core.registry.InstanceRegistry
import org.koin.dsl.module

fun createKoinTestModuleRule(block: Module.() -> Unit): TestRule = KoinTestModuleRule(block)

private class KoinTestModuleRule(private val block: Module.() -> Unit) : TestWatcher() {

    private val restoringKoinModule = RestoringKoinModule()

    override fun starting(description: Description?) {
        super.starting(description)
        val module = module(createdAtStart = false, block)
        restoringKoinModule.load(module)
    }

    override fun finished(description: Description?) {
        restoringKoinModule.restore()
        super.finished(description)
    }
}

@OptIn(KoinInternalApi::class)
private class RestoringKoinModule {

    private val testModules = mutableListOf<Module>()
    private val overriddenMappings = mutableMapOf<IndexKey, InstanceFactory<*>>()

    private val koinRegistry: InstanceRegistry?
        get() = GlobalContext.getKoinApplicationOrNull()?.koin?.instanceRegistry

    fun load(module: Module) {
        testModules += module
        saveOverriddenDependencies(module)
        loadKoinModules(module)
    }

    fun restore() {
        testModules.forEach { unloadKoinModules(it) }
        testModules.clear()
        restoreOverriddenDependencies()
    }

    private fun saveOverriddenDependencies(module: Module) {
        module.mappings
            .filterNot { overriddenMappings.containsKey(it.key) }
            .forEach { key, _ ->
                koinRegistry?.instances?.get(key)?.also { overriddenMappings += key to it }
            }
    }

    private fun restoreOverriddenDependencies() {
        overriddenMappings.forEach { key, value ->
            koinRegistry?.saveMapping(
                allowOverride = true,
                mapping = key,
                factory = value,
                logWarning = true
            )
        }
        overriddenMappings.clear()
    }
}
