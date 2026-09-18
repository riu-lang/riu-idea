package org.riu.lang.ide.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import org.riu.lang.ide.RiuFileType
import org.riu.lang.ide.RiuLanguage

class RiuFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, RiuLanguage) {
    override fun getFileType(): FileType = RiuFileType
    override fun toString(): String = "Riu File"
}
