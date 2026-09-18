package org.riu.lang.ide

import com.intellij.lang.Language

object RiuLanguage : Language("riu") {
    private fun readResolve(): Any = RiuLanguage
}
