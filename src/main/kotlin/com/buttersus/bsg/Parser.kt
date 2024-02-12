@file:Suppress("RedundantNullableReturnType")

package com.buttersus.bsg

import com.buttersus.gramutils.*

class Parser<NB : Node<NB>> : ParserBase<
        Parser<NB>, NB, Node.Wrapper,
        Lexer, TokenType, Token>() {
    // Create methods
    override fun createWrapperNode(`𝚝`: Token) = Node.Wrapper(`𝚝`)

    override fun <N : NodeBase<N>> createGroupNode(nodes: List<Opt<N>>): NodeGroupBase<N, *> {
        @Suppress("UNCHECKED_CAST")
        return Node.Group(*nodes.toTypedArray() as Array<Opt<NB>>) as NodeGroupBase<N, *>
    }

    override fun <N : NodeBase<N>> createDynamicGroupNode(nodes: List<Opt<NB>>): NodeDynamicGroupBase<N, *> {
        @Suppress("UNCHECKED_CAST")
        return Node.DynamicGroup(*nodes.toTypedArray()) as NodeDynamicGroupBase<N, *>
    }

    override fun raiseSyntaxException(`𝚙ₛ`: Position, `𝚙ₑ`: Position, `𝚝`: String) =
        throw SyntaxException(`𝚙ₛ`, `𝚙ₑ`, `𝚝`)

    // Custom productions
    override fun parse() = logger.info { "Starting..." }.let { `file`().also { logger.info { "Finished" } } }

    private fun `file`() = `𝚖`(
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

    private fun `statement`() = `𝚖`(
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

    private fun `modifiers`() = `𝚖`(
        "modifiers", false,
        // {'main' | 'public' | 'private' | 'protected'}* => Self
        ::`∨⊛`.`→`(
            ::`≡`.`→`("main"),
            ::`≡`.`→`("public"),
            ::`≡`.`→`("private"),
            ::`≡`.`→`("protected")
        )
    )

    private fun `identifier`() = `𝚖`(
        "identifier", false,
        // <CNAME> | <NAME> => Self
        ::`∨`.`→`(
            ::`≈`.`→`(TokenType.CNAME),
            ::`≈`.`→`(TokenType.NAME)
        )
    )

    private fun `node`() = `𝚖`(
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

    private fun `basic-PEG`() = `𝚖`(
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

    private fun `elementary-PEG`() = `𝚖`(
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
