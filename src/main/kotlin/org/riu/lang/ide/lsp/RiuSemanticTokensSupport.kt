package org.riu.lang.ide.lsp

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.platform.lsp.api.customization.LspSemanticTokensSupport
import com.intellij.psi.PsiFile
import org.riu.lang.ide.RiuLanguage
import org.riu.lang.ide.highlighter.RiuColors

/**
 * 只把 riu-lsp 发来的语义分类映到 RiuColors；词法级（关键字/字符串等）由 SyntaxHighlighter 负责。
 */
class RiuSemanticTokensSupport : LspSemanticTokensSupport() {
    override fun shouldAskServerForSemanticTokens(psiFile: PsiFile): Boolean =
        psiFile.language == RiuLanguage

    override fun getTextAttributesKey(
        tokenType: String,
        modifiers: List<String>,
    ): TextAttributesKey? {
        val isDecl = "declaration" in modifiers
        return when (tokenType) {
            // riu-lsp 不再发词法级 semantic token；若收到则跳过，避免盖住 SyntaxHighlighter
            "keyword", "operator", "string", "number", "comment", "variable" -> null
            "class" -> RiuColors.CLASS
            "interface" -> RiuColors.INTERFACE
            "enum" -> RiuColors.ENUM
            "enumMember" -> RiuColors.ENUM_MEMBER
            "function" -> if (isDecl) RiuColors.FUNCTION_DECLARATION else RiuColors.FUNCTION_CALL
            "method" -> if (isDecl) RiuColors.FUNCTION_DECLARATION else RiuColors.FUNCTION_CALL
            "property" -> RiuColors.PROPERTY
            "parameter" -> RiuColors.PARAMETER
            "metadata" -> RiuColors.METADATA
            else -> null
        }
    }
}
