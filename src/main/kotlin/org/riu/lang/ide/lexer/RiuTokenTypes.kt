package org.riu.lang.ide.lexer

import com.intellij.psi.tree.IElementType
import org.riu.lang.ide.RiuLanguage

class RiuTokenType(name: String) : IElementType(name, RiuLanguage)

object RiuTokenTypes {
    @JvmField val LINE_COMMENT = RiuTokenType("RIU_LINE_COMMENT")
    @JvmField val STRING = RiuTokenType("RIU_STRING")
    @JvmField val CODE_POINT = RiuTokenType("RIU_CODE_POINT")
    @JvmField val NUMBER = RiuTokenType("RIU_NUMBER")
    @JvmField val KEYWORD = RiuTokenType("RIU_KEYWORD")
    @JvmField val METADATA = RiuTokenType("RIU_METADATA")
    @JvmField val IDENTIFIER = RiuTokenType("RIU_IDENTIFIER")
    @JvmField val SYMBOL = RiuTokenType("RIU_SYMBOL")

    val KEYWORDS = setOf(
        "break", "catch", "continue", "elif", "else", "enum", "extern", "false", "fn", "for", "if", "in", "let",
        "loop", "match", "null", "ret", "Self", "struct", "true", "try", "use"
    )
}
