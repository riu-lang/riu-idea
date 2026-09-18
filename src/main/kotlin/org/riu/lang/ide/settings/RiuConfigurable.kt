package org.riu.lang.ide.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.SystemInfo
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Settings → Languages & Frameworks → Riu
 *
 * 配置 riu 主目录（Home），插件会以 <home>/bin/riu(.exe) 启动 LSP 服务，
 * 同时该目录下应包含 sdk/ 等子目录，供编译器查找运行时。
 */
class RiuConfigurable(private val project: Project) : Configurable {
    private var panel: JPanel? = null
    private val homeField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
                .withTitle("Riu Home Directory")
                .withDescription("选择 riu 安装根目录（包含 bin/、sdk/）"),
        )
    }
    private val statusLabel = JBLabel()

    override fun getDisplayName(): String = "Riu"

    override fun createComponent(): JComponent {
        homeField.textField.document.addDocumentListener(
            object : javax.swing.event.DocumentListener {
                override fun insertUpdate(e: javax.swing.event.DocumentEvent?) = refreshStatus()
                override fun removeUpdate(e: javax.swing.event.DocumentEvent?) = refreshStatus()
                override fun changedUpdate(e: javax.swing.event.DocumentEvent?) = refreshStatus()
            },
        )
        val built = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Riu home:"), homeField, 1, false)
            .addComponentToRightColumn(statusLabel, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
        panel = built
        return built
    }

    override fun isModified(): Boolean =
        homeField.text.trim() != RiuSettings.getInstance(project).homePath

    override fun apply() {
        RiuSettings.getInstance(project).homePath = homeField.text.trim()
    }

    override fun reset() {
        homeField.text = RiuSettings.getInstance(project).homePath
        refreshStatus()
    }

    override fun disposeUIResources() {
        panel = null
    }

    private fun refreshStatus() {
        val home = homeField.text.trim()
        if (home.isEmpty()) {
            statusLabel.text = "未设置，将使用 PATH 中的 riu"
            return
        }
        val exeName = if (SystemInfo.isWindows) "riu.exe" else "riu"
        val exe = Paths.get(home, "bin", exeName)
        val sdk = Paths.get(home, "sdk")
        val parts = buildList {
            add(if (Files.isRegularFile(exe)) "✔ bin/$exeName" else "✘ bin/$exeName 未找到")
            add(if (Files.isDirectory(sdk)) "✔ sdk/" else "✘ sdk/ 未找到")
        }
        statusLabel.text = parts.joinToString("    ")
    }
}
