package org.riu.lang.ide.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerDescriptor
import com.intellij.platform.lsp.api.customization.LspCustomization
import org.riu.lang.ide.RiuFileType
import org.riu.lang.ide.settings.RiuSettings

class RiuLspServerDescriptor(
    project: Project,
    root: VirtualFile,
) : LspServerDescriptor(project, "Riu Language Server", root) {
    override fun isSupportedFile(file: VirtualFile): Boolean = file.fileType == RiuFileType

    override fun getLanguageId(file: VirtualFile): String = "riu"

    override fun createCommandLine(): GeneralCommandLine {
        val settings = RiuSettings.getInstance(project)
        return GeneralCommandLine(settings.resolveExecutable()).apply {
            withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            withWorkDirectory(roots.first().path)
            if (settings.homePath.isNotEmpty()) {
                environment["RIU_HOME"] = settings.homePath
            }
        }
    }

    override val lspCustomization: LspCustomization = object : LspCustomization() {
        override val semanticTokensCustomizer = RiuSemanticTokensSupport()
    }
}
