@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package com.buttersus.blg

import com.buttersus.gramutils.*

sealed class Node : NodeBase<Node>() {
    // GramUtils nodes
    object Empty : Node(), NodeEmptyBase {
        override fun toString(): String = "Empty"
    }
    class Wrapper(override val `𝚝`: Token) : Node(), NodeWrapperBase<TokenType, Token> {
        override fun toString(): String = "Wrapper($`𝚝`)"
    }
    class Group(vararg nodes: Node) : Node(), List<Node> by listOf(*nodes), NodeGroupBase<Node, Group> {
        override fun createGroup(nodes: List<Node>): Group = Group(*nodes.toTypedArray())
        override fun toString(): String = "Group(${joinToString(", ")})"
    }

    class DynamicGroup(vararg nodes: Node) : Node(),
        MutableList<Node> by mutableListOf(*nodes), NodeDynamicGroupBase<Node, DynamicGroup> {
        override fun toString(): String = "DynamicGroup(${joinToString(", ")})"
    }

    // Custom nodes
    // => File(nodes)
    data class File(
        val statements: Node,
    ) : Node()

    // => Statement(modifiers, name, nodes)
    data class Statement(
        val modifiers: Node,
        val name: Node,
        val nodes: Node,
    ) : Node()

    // => Kleene(pattern, type = $enumStringMap(KleeneType, '*': STAR, '+': PLUS, '?': QUESTION)
    data class Kleene(
        val pattern: Node,
        val type: KleeneType,
    ) : Node() {
        companion object {
            fun `𝚌₁`(pattern: Node, `𝚜₁`: Node) = Kleene(pattern, KleeneType.fromString(`𝚜₁`.`𝚟`))
        }

        enum class KleeneType {
            STAR, PLUS, QUESTION;

            companion object {
                fun fromString(string: String): KleeneType = when (string) {
                    "*" -> STAR
                    "+" -> PLUS
                    "?" -> QUESTION
                    else -> throw IllegalArgumentException("Unknown parameter: $string")
                }
            }
        }

        override fun getParametersMap() = mapOf(
            "type" to type
        )
    }
}
