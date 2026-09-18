package org.riu.lang.ide.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service(Service.Level.PROJECT)
@State(name = "RiuSettings", storages = [Storage("riu.xml")])
class RiuSettings : PersistentStateComponent<RiuSettings.State> {
    data class State(
        // riu 主目录，期望布局：
        //   <home>/bin/riu(.exe)
        //   <home>/sdk/riu/core/*.ut
        var homePath: String = "",
    )

    private var state = State()

    override fun getState(): State = state
    override fun loadState(state: State) {
        this.state = state
    }

    var homePath: String
        get() = state.homePath
        set(value) {
            state.homePath = value.trim()
        }

    /**
     * 解析最终用于启动 LSP 的可执行文件路径：
     * - 配置了 homePath：返回 <home>/bin/riu-lsp(.exe)
     * - 否则：返回 "riu-lsp"（依赖 PATH）
     */
    fun resolveExecutable(): String {
        val home = state.homePath.trim()
        if (home.isNotEmpty()) {
            val exeName = if (SystemInfo.isWindows) "riu-lsp.exe" else "riu-lsp"
            val path: Path = Paths.get(home, "bin", exeName)
            if (Files.isRegularFile(path)) {
                return path.toAbsolutePath().toString()
            }
        }
        return "riu-lsp"
    }

    companion object {
        fun getInstance(project: Project): RiuSettings = project.service()
    }
}
