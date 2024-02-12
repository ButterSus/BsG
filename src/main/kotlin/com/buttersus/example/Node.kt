@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package com.buttersus.example

import com.buttersus.gramutils.*

sealed class Node : NodeBase<Node>() {
    // GramUtils nodes
    object Empty : Node(), NodeEmptyBase {
        override fun toString(): String = "Empty"
    }
    class Wrapper(override val `𝚝`: Token) : Node(), NodeWrapperBase<TokenType, Token> {
        override fun toString(): String = "Wrapper($`𝚝`)"
        override val `𝚙ₛ`: Position = `𝚝`.`𝚙ₛ`
        override val `𝚙ₑ`: Position = `𝚝`.`𝚙ₑ`
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
    // => Example(arg1)
    data class Example(
        val arg1: Node
    )
}
