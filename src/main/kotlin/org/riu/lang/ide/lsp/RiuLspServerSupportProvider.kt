package org.riu.lang.ide.lsp

import com.intellij.openapi.project.BaseProjectDirectories
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.lsWidget.LspServerWidgetItem
import org.riu.lang.ide.RiuFileType
import org.riu.lang.ide.RiuIcons
import org.riu.lang.ide.settings.RiuConfigurable

class RiuLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(
        project: Project,
        file: VirtualFile,
        serverStarter: LspServerSupportProvider.LspServerStarter,
    ) {
        if (file.fileType != RiuFileType) return

        val root = BaseProjectDirectories.getInstance(project).getBaseDirectoryFor(file)
            ?: file.parent
            ?: return

        serverStarter.ensureServerStarted(RiuLspServerDescriptor(project, root))
    }

    override fun createLspServerWidgetItem(
        lspServer: LspServer,
        currentFile: VirtualFile?,
    ): LspServerWidgetItem? =
        LspServerWidgetItem(lspServer, currentFile, RiuIcons.FILE, RiuConfigurable::class.java)
}
