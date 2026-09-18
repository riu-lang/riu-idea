package org.riu.lang.ide.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.riu.lang.ide.RiuLanguage
import org.riu.lang.ide.lexer.RiuLexer
import org.riu.lang.ide.lexer.RiuTokenTypes
import org.riu.lang.ide.psi.RiuFile

class RiuParserDefinition : ParserDefinition {

    override fun createLexer(project: Project?): Lexer = RiuLexer()

    override fun createParser(project: Project?): PsiParser = RiuFlatParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRINGS

    override fun createElement(node: ASTNode): PsiElement =
        com.intellij.psi.impl.source.tree.LeafPsiElement(node.elementType, node.text)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = RiuFile(viewProvider)

    companion object {
        val FILE = IFileElementType(RiuLanguage)
        val COMMENTS: TokenSet = TokenSet.create(RiuTokenTypes.LINE_COMMENT)
        val STRINGS: TokenSet = TokenSet.create(RiuTokenTypes.STRING, RiuTokenTypes.CODE_POINT)
    }
}
