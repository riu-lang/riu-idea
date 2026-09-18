package org.riu.lang.ide.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

/**
 * 平铺 parser：所有 token 都作为根节点的叶子，不构造任何中间结构。
 * 仅用于满足 IntelliJ "已解析" 前提，让编辑器拥有 token 边界。
 */
class RiuFlatParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val mark = builder.mark()
        while (!builder.eof()) {
            builder.advanceLexer()
        }
        mark.done(root)
        return builder.treeBuilt
    }
}
