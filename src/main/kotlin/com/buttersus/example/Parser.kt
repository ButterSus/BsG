@file:Suppress("RedundantNullableReturnType")

package com.buttersus.example

import com.buttersus.gramutils.*

class Parser : ParserBase<
        Parser, Node, Node.Wrapper, Node.Empty, SyntaxException,
        Node.Group, Node.DynamicGroup, Lexer, TokenType, Token>() {
    // Create methods
    override fun createWrapperNode(`𝚝`: Token) = Node.Wrapper(`𝚝`)
    override fun createEmptyNode() = Node.Empty
    override fun createGroupNode(nodes: List<Node>) = Node.Group(*nodes.toTypedArray())
    override fun createDynamicGroupNode(nodes: List<Node>) = Node.DynamicGroup(*nodes.toTypedArray())
    override fun raiseSyntaxException(`𝚙ₛ`: Position, `𝚙ₑ`: Position, `𝚝`: String) =
        throw SyntaxException(`𝚙ₛ`, `𝚙ₑ`, `𝚝`)

    // Custom productions
    override fun parse(): Node? {
        logger.info { "Starting..." }
        return `file`().also { logger.info { "Finished" } }
    }

    private fun `file`(): Node? = `𝚖`(
        "file", false,
        TODO("Implement file production")
    )
}
