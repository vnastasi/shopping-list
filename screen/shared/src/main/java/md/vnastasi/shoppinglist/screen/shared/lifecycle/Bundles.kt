@file:JvmName("BundleSupport")

package md.vnastasi.shoppinglist.screen.shared.lifecycle

import android.os.Bundle
import androidx.core.os.bundleOf

fun Bundle?.orEmpty(): Bundle = this ?: bundleOf()

operator fun Bundle?.plus(other: Bundle): Bundle = this.orEmpty().apply { putAll(other) }
