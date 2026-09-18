package org.riu.lang.ide.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

/**
 * 手写 Lexer：提供 IntelliJ token 边界。
 * 识别关键字、注释、字符串、代码点、数字、标识符、符号。
 * 语义高亮（类/函数/字段/参数等）由 LSP semantic tokens 提供。
 */
class RiuLexer : LexerBase() {

    private var buffer: CharSequence = ""
    private var endOffset: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var tokenType: IElementType? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.endOffset = endOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        advance()
    }

    override fun getState(): Int = 0
    override fun getTokenType(): IElementType? = tokenType
    override fun getTokenStart(): Int = tokenStart
    override fun getTokenEnd(): Int = tokenEnd
    override fun getBufferSequence(): CharSequence = buffer
    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        tokenStart = tokenEnd
        if (tokenStart >= endOffset) {
            tokenType = null
            return
        }

        val c = buffer[tokenStart]

        // 空白（含换行）
        if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
            var i = tokenStart + 1
            while (i < endOffset) {
                val ch = buffer[i]
                if (ch != ' ' && ch != '\t' && ch != '\r' && ch != '\n') break
                i++
            }
            tokenEnd = i
            tokenType = TokenType.WHITE_SPACE
            return
        }

        // 行注释 `;` 到行尾（不含换行）
        if (c == ';') {
            var i = tokenStart + 1
            while (i < endOffset && buffer[i] != '\n' && buffer[i] != '\r') i++
            tokenEnd = i
            tokenType = RiuTokenTypes.LINE_COMMENT
            return
        }

        // 字符串 "..."
        if (c == '"') {
            tokenEnd = scanDoubleQuoted(tokenStart + 1)
            tokenType = RiuTokenTypes.STRING
            return
        }

        // 原始字符串 r"..."
        if (c == 'r' && tokenStart + 1 < endOffset && buffer[tokenStart + 1] == '"') {
            var i = tokenStart + 2
            while (i < endOffset) {
                val ch = buffer[i]
                if (ch == '\n' || ch == '\r') break
                if (ch == '"') { i++; break }
                i++
            }
            tokenEnd = i
            tokenType = RiuTokenTypes.STRING
            return
        }

        // 代码点 c'...'
        if (c == 'c' && tokenStart + 1 < endOffset && buffer[tokenStart + 1] == '\'') {
            val end = scanCodePoint(tokenStart + 2)
            if (end > 0) {
                tokenEnd = end
                tokenType = RiuTokenTypes.CODE_POINT
                return
            }
            // 不是合法代码点，按标识符处理
        }

        // 数字
        if (c in '0'..'9') {
            tokenEnd = scanNumber(tokenStart)
            tokenType = RiuTokenTypes.NUMBER
            return
        }

        // 构建注解 #Name
        if (c == '#' && tokenStart + 1 < endOffset && isIdStart(buffer[tokenStart + 1])) {
            var i = tokenStart + 2
            while (i < endOffset && isIdPart(buffer[i])) i++
            tokenEnd = i
            tokenType = RiuTokenTypes.METADATA
            return
        }

        // 标识符或关键字（含非 ASCII）
        if (isIdStart(c)) {
            var i = tokenStart + 1
            while (i < endOffset && isIdPart(buffer[i])) i++
            tokenEnd = i
            val text = buffer.subSequence(tokenStart, tokenEnd).toString()
            tokenType = if (text in RiuTokenTypes.KEYWORDS) RiuTokenTypes.KEYWORD else RiuTokenTypes.IDENTIFIER
            return
        }

        // 其它单字符 → SYMBOL（运算符/括号/逗号/点/分号/#/: 等都归这一类）
        tokenEnd = tokenStart + 1
        tokenType = RiuTokenTypes.SYMBOL
    }

    /** 双引号字符串：从开引号之后开始扫，含 `\` 转义，遇换行或闭引号停。返回结束 offset。 */
    private fun scanDoubleQuoted(from: Int): Int {
        var i = from
        while (i < endOffset) {
            val ch = buffer[i]
            if (ch == '\n' || ch == '\r') return i
            if (ch == '\\' && i + 1 < endOffset) { i += 2; continue }
            if (ch == '"') return i + 1
            i++
        }
        return i
    }

    /** c'...' 代码点：from 指向开单引号之后。失败返回 -1。 */
    private fun scanCodePoint(from: Int): Int {
        if (from >= endOffset) return -1
        var i = from
        val ch = buffer[i]
        if (ch == '\n' || ch == '\r' || ch == '\'') return -1
        i += if (ch == '\\' && i + 1 < endOffset) 2 else 1
        if (i >= endOffset || buffer[i] != '\'') return -1
        return i + 1
    }

    /** 数字：宽集合贪婪吞，含进制前缀/小数点/指数/后缀；仅为 IDE 边界用。 */
    private fun scanNumber(from: Int): Int {
        var i = from
        // 主体：digit / hex / 进制字母 / 下划线 / 点
        while (i < endOffset) {
            val ch = buffer[i]
            if (ch in '0'..'9' || ch in 'a'..'f' || ch in 'A'..'F'
                || ch == 'x' || ch == 'X' || ch == 'b' || ch == 'B'
                || ch == 'o' || ch == 'O' || ch == '_' || ch == '.') {
                i++
            } else break
        }
        // 指数 [eE][+-]?[0-9]+：上面 e 已被吞，回退处理由 LSP 把关；这里只补可能的 +/-/digits
        // 后缀 i/u/f + 位宽
        if (i < endOffset) {
            val ch = buffer[i]
            if (ch == 'i' || ch == 'u' || ch == 'f') {
                i++
                while (i < endOffset && buffer[i] in '0'..'9') i++
            }
        }
        return i
    }

    /** 按 g4 ID 起始集合：排除 ASCII 标点/数字/反引号/DEL/空白；其余（含非 ASCII）皆可。 */
    private fun isIdStart(c: Char): Boolean {
        if (c.isWhitespace()) return false
        val code = c.code
        if (code in 0x21..0x40) return false      // ! " # $ % & ' ( ) * + , - . / 0-9 : ; < = > ? @
        if (code in 0x5B..0x5E) return false      // [ \ ] ^
        if (code == 0x60) return false            // `
        if (code in 0x7B..0x7F) return false      // { | } ~ DEL
        return true
    }

    /** ID 续接：相比 start 额外允许 0-9。 */
    private fun isIdPart(c: Char): Boolean {
        if (c in '0'..'9') return true
        return isIdStart(c)
    }
}
