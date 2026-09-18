package org.riu.lang.ide.highlighter

import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import org.riu.lang.ide.RiuIcons
import javax.swing.Icon

class RiuColorSettingsPage : ColorSettingsPage {

    override fun getDisplayName(): String = "Riu"

    override fun getIcon(): Icon = RiuIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = RiuSyntaxHighlighter()

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDemoText(): String = """
        ; 行注释
        use riu.core.*

        struct N {
          v i32
        }

        N {
          fn N(v i32) {
            ${'$'}.v = v
          }

          fn plus(other N) N {
            N(${'$'}.v + other.v)
          }
        }

        fn main() {
          let n = N(1)
          let n2 = N(2)
          let n3 = n.plus(n2)
          println(n3.v.to_string())
        }
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, com.intellij.openapi.editor.colors.TextAttributesKey>? = null

    companion object {
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor("注释",       RiuColors.LINE_COMMENT),
            AttributesDescriptor("字符串",     RiuColors.STRING),
            AttributesDescriptor("代码点",     RiuColors.CODE_POINT),
            AttributesDescriptor("数字",       RiuColors.NUMBER),
            AttributesDescriptor("关键字",     RiuColors.KEYWORD),
            AttributesDescriptor("运算符",     RiuColors.OPERATOR),
            AttributesDescriptor("构建注解",   RiuColors.METADATA),
            AttributesDescriptor("标识符",     RiuColors.IDENTIFIER),
            AttributesDescriptor("变量",       RiuColors.VARIABLE),
            AttributesDescriptor("参数",       RiuColors.PARAMETER),
            AttributesDescriptor("结构体字段", RiuColors.PROPERTY),
            AttributesDescriptor("结构体/类型", RiuColors.CLASS),
            AttributesDescriptor("接口 (spec)", RiuColors.INTERFACE),
            AttributesDescriptor("枚举",        RiuColors.ENUM),
            AttributesDescriptor("枚举成员",    RiuColors.ENUM_MEMBER),
            AttributesDescriptor("函数声明",    RiuColors.FUNCTION_DECLARATION),
            AttributesDescriptor("函数调用",    RiuColors.FUNCTION_CALL),
            AttributesDescriptor("方法 / 构造", RiuColors.METHOD),
        )
    }
}
