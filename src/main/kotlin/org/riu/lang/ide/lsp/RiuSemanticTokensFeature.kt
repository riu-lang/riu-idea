package org.riu.lang.ide.lsp

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiFile
import com.redhat.devtools.lsp4ij.client.features.LSPSemanticTokensFeature
import org.riu.lang.ide.highlighter.RiuColors

/**
 * 把 riu LSP server 发送的 semantic token 类型显式映到 RiuColors 里的可配置 key。
 * 用户在 Settings → Editor → Color Scheme → Riu 改色后，这里返回的 key 自动跟随。
 */
class RiuSemanticTokensFeature : LSPSemanticTokensFeature() {
    override fun getTextAttributesKey(
        tokenType: String,
        tokenModifiers: MutableList<String>,
        file: PsiFile
    ): TextAttributesKey? {
        val isDecl = "declaration" in tokenModifiers
        return when (tokenType) {
            "keyword"   -> RiuColors.KEYWORD
            "operator"  -> RiuColors.OPERATOR
            "string"    -> RiuColors.STRING
            "number"    -> RiuColors.NUMBER
            "comment"   -> RiuColors.LINE_COMMENT
            "variable"  -> RiuColors.VARIABLE
            "class"     -> RiuColors.CLASS
            "interface" -> RiuColors.INTERFACE
            "enum"       -> RiuColors.ENUM
            "enumMember" -> RiuColors.ENUM_MEMBER
            "function"  -> if (isDecl) RiuColors.FUNCTION_DECLARATION else RiuColors.FUNCTION_CALL
            "method"    -> if (isDecl) RiuColors.FUNCTION_DECLARATION else RiuColors.FUNCTION_CALL
            "property"  -> RiuColors.PROPERTY
            "parameter" -> RiuColors.PARAMETER
            "metadata"  -> RiuColors.METADATA
            else        -> super.getTextAttributesKey(tokenType, tokenModifiers, file)
        }
    }
}
