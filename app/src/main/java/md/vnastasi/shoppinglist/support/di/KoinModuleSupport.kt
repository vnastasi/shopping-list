package md.vnastasi.shoppinglist.support.di

import android.app.Activity
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.DefinitionOptions
import org.koin.core.module.dsl.new
import org.koin.core.module.dsl.onOptions
import org.koin.core.qualifier.Qualifier

inline fun <reified T : Activity> Module.activity(
    qualifier: Qualifier? = null,
    noinline definition: Definition<T>
): KoinDefinition<T> = factory(qualifier, definition)

inline fun <reified R : Activity, reified T1> Module.activityOf(
    crossinline constructor: (T1) -> R,
    noinline options: DefinitionOptions<R>? = null,
): KoinDefinition<R> = activity { new(constructor) }.onOptions(options)
