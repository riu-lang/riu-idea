package org.riu.lang.ide.lsp

import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.server.ProcessStreamConnectionProvider
import org.riu.lang.ide.settings.RiuSettings

class RiuLspServer(project: Project) : ProcessStreamConnectionProvider() {
    init {
        val settings = RiuSettings.getInstance(project)
        val executable = settings.resolveExecutable()
        val cwd = project.basePath
        super.setCommands(listOf(executable))
        if (cwd != null) {
            super.setWorkingDirectory(cwd)
        }
        // 把 riu 主目录透传给子进程，便于编译器在非默认布局下定位 sdk/。
        if (settings.homePath.isNotEmpty()) {
            super.setUserEnvironmentVariables(mapOf("RIU_HOME" to settings.homePath))
        }
    }
}
