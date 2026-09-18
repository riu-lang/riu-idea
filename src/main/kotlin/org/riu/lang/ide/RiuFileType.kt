package org.riu.lang.ide

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object RiuFileType : LanguageFileType(RiuLanguage) {
    override fun getName(): String = "Riu File"
    override fun getDescription(): String = "Riu language source file"
    override fun getDefaultExtension(): String = "riu"
    override fun getIcon(): Icon = RiuIcons.FILE
}
