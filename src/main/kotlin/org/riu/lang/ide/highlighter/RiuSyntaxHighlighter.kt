package org.riu.lang.ide.highlighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.riu.lang.ide.lexer.RiuLexer
import org.riu.lang.ide.lexer.RiuTokenTypes

/**
 * 基础语法高亮器：提供注释、字符串、代码点、数字、关键字的高亮。
 * 语义高亮（类/函数/字段/参数等）由 LSP semantic tokens 提供。
 * 在 markdown 代码块等内嵌场景下，LSP 无法工作，此时仅使用此基础高亮。
 */
class RiuSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = RiuLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        when (tokenType) {
            RiuTokenTypes.LINE_COMMENT -> COMMENT
            RiuTokenTypes.STRING       -> STRING
            RiuTokenTypes.CODE_POINT   -> CODE_POINT
            RiuTokenTypes.NUMBER       -> NUMBER
            RiuTokenTypes.KEYWORD      -> KEYWORD
            RiuTokenTypes.METADATA     -> METADATA
            else -> EMPTY
        }

    companion object {
        private val COMMENT    = arrayOf(RiuColors.LINE_COMMENT)
        private val STRING     = arrayOf(RiuColors.STRING)
        private val CODE_POINT = arrayOf(RiuColors.CODE_POINT)
        private val NUMBER     = arrayOf(RiuColors.NUMBER)
        private val KEYWORD    = arrayOf(RiuColors.KEYWORD)
        private val METADATA   = arrayOf(RiuColors.METADATA)
        private val EMPTY      = emptyArray<TextAttributesKey>()
    }
}
