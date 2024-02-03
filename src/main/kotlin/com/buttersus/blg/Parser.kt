@file:Suppress(
    "MemberVisibilityCanBePrivate", "SameParameterValue",
    "Unused", "RedundantNullableReturnType"
)

package com.buttersus.blg

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
        return file().also { logger.info { "Finished" } }
    }

    private fun file(): Node? = `𝚖`(
        "file", false,
        ::`∨`.`→`(
            // statement:<NEWLINE>* => File(statements)
            Node::File.`→…`(
                ::`⊛̂`.`→`(
                    ::`statement`,
                    ::`≈`.`→`(TokenType.NEWLINE)
                )
            )
        )
    )

    private fun `statement`(): Node? = `𝚖`(
        "statement", false,
        // .modifiers .identifier !':' -> "Expected ':'"
        // <NEWLINE> <INDENT> .node:<NEWLINE>+ <NEWLINE>?
        // <DEDENT> => Statement(modifiers, name, nodes)
        Node::Statement.`→…`(
            setOf(1),
            ::`{…}`.`→`(
                ::`modifiers`,
                ::`identifier`,
                ::`!`.`→`("Expected ':'", ::`≡`.`→`(":")),
                ::`≈`.`→`(TokenType.NEWLINE),
                ::`≈`.`→`(TokenType.INDENT),
                ::`⊕̂`.`→`(
                    ::`node`,
                    ::`≈`.`→`(TokenType.NEWLINE)
                ),
                ::`∅`.`→`(::`≈`.`→`(TokenType.NEWLINE)),
                ::`≈`.`→`(TokenType.DEDENT)
            ).select(1, 2, 6)
        ).withReset()
    )

    private fun `modifiers`(): Node? = `𝚖`(
        "modifiers", false,
        // {'main' | 'public' | 'private' | 'protected'}* => Self
        ::`∨⊛`.`→`(
            ::`≡`.`→`("main"),
            ::`≡`.`→`("public"),
            ::`≡`.`→`("private"),
            ::`≡`.`→`("protected")
        )
    )

    private fun `identifier`(): Node? = `𝚖`(
        "identifier", false,
        // <CNAME> | <NAME> => Self
        ::`∨`.`→`(
            ::`≈`.`→`(TokenType.CNAME),
            ::`≈`.`→`(TokenType.NAME)
        )
    )

    private fun `node`(): Node? = `𝚖`(
        "node", false,
        // .element+ !'=>' -> "Expected '=>'" .result => Self
        ::`{…}`.`→`(
            ::`⊕`.`→`(
                ::`basic-PEG`
            ),
            ::`!`.`→`("Expected '=>'", ::`≡`.`→`("=>")),
            ::`result`
        ).select(1, 3)
    )

    private fun `basic-PEG`(): Node? = `𝚖`(
        "basic-PEG", false,
        ::`∨`.`→`(
            // elementary-node {'*' | '+' | '?'} => Kleene(pattern, type = $enumStringMap(KleeneType, '*': STAR, '+': PLUS, '?': QUESTION)
            Node.Kleene.Companion::`𝚌₁`.`→…`(
                ::`elementary-PEG`,
                ::`∨`.`→`(
                    ::`≡`.`→`("*"),
                    ::`≡`.`→`("+"),
                    ::`≡`.`→`("?")
                )
            ).withReset(),
            // elementary-node => Self
            ::`elementary-PEG`
        )
    )

    private fun `elementary-PEG`(): Node? = `𝚖`(
        "elementary-PEG", false,
        ::`∨`.`→`(
            // identifier => Self
            ::`identifier`
        )
    )

    private fun `result`(): Node? = `𝚖`(
        "result", false,
        // 'Self'
        ::`≡`.`→`("Self")
    )
}
