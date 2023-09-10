@file:JvmName("BundleSupport")

package md.vnastasi.shoppinglist.support.bundle

import android.os.Bundle
import androidx.core.os.bundleOf

fun Bundle?.orEmpty(): Bundle = this ?: bundleOf()
