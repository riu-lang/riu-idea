package org.riu.lang.ide.highlighter

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default
import com.intellij.openapi.editor.colors.TextAttributesKey

/**
 * riu 语言所有可在 Settings → Editor → Color Scheme → Riu 中调整的颜色键。
 * fallback 指向 IntelliJ 通用类别，主题切换时自动跟随。
 */
object RiuColors {
    val LINE_COMMENT  = key("RIU_LINE_COMMENT", Default.LINE_COMMENT)
    val STRING        = key("RIU_STRING",       Default.STRING)
    val CODE_POINT    = key("RIU_CODE_POINT",   Default.STRING)
    val NUMBER        = key("RIU_NUMBER",       Default.NUMBER)
    val KEYWORD       = key("RIU_KEYWORD",      Default.KEYWORD)
    val OPERATOR      = key("RIU_OPERATOR",     Default.OPERATION_SIGN)
    val METADATA      = key("RIU_METADATA",     Default.METADATA)

    val IDENTIFIER    = key("RIU_IDENTIFIER",   Default.IDENTIFIER)
    val VARIABLE      = key("RIU_VARIABLE",     Default.LOCAL_VARIABLE)
    val PARAMETER     = key("RIU_PARAMETER",    Default.PARAMETER)
    val PROPERTY      = key("RIU_PROPERTY",     Default.INSTANCE_FIELD)

    val CLASS                = key("RIU_CLASS",                Default.CLASS_NAME)
    val INTERFACE            = key("RIU_INTERFACE",            Default.INTERFACE_NAME)
    val ENUM                 = key("RIU_ENUM",                 Default.CLASS_NAME)
    val ENUM_MEMBER          = key("RIU_ENUM_MEMBER",          Default.STATIC_FIELD)
    val FUNCTION_DECLARATION = key("RIU_FUNCTION_DECLARATION", Default.FUNCTION_DECLARATION)
    val FUNCTION_CALL        = key("RIU_FUNCTION_CALL",        Default.FUNCTION_CALL)
    val METHOD               = key("RIU_METHOD",               Default.INSTANCE_METHOD)

    private fun key(name: String, fallback: TextAttributesKey): TextAttributesKey =
        TextAttributesKey.createTextAttributesKey(name, fallback)
}
