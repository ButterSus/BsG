@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package com.buttersus.bsg

import com.buttersus.gramutils.*

sealed class Node<NB : Node<NB>> : NodeBase<NB>() {
    // GramUtils nodes
    object Empty : Node<Empty>(), NodeEmptyBase {
        override fun toString(): String = "Empty"
    }

    class Wrapper(override val `𝚝`: Token) : Node<Wrapper>(), NodeWrapperBase<TokenType, Token> {
        override fun toString(): String = "Wrapper($`𝚝`)"
        override val `𝚙ₛ`: Position = `𝚝`.`𝚙ₛ`
        override val `𝚙ₑ`: Position = `𝚝`.`𝚙ₑ`
    }

    class Group<T : Node<T>>(vararg nodes: Opt<T>) : Node<T>(),
        List<Opt<T>> by listOf(*nodes), NodeGroupBase<T, Group<T>> {
        override fun createGroup(nodes: List<Opt<T>>): Group<T> = Group(*nodes.toTypedArray())
        override fun item(`𝚒`: Int): Opt<T> = this[`𝚒` - 1]
        override fun toString(): String = "Group(${joinToString(", ")})"
    }

    class DynamicGroup<T : Node<T>>(vararg nodes: Opt<T>) : Node<T>(),
        MutableList<Opt<T>> by mutableListOf(*nodes), NodeDynamicGroupBase<T, DynamicGroup<T>> {
        override fun item(`𝚒`: Int): Opt<T> = this[`𝚒` - 1]
        override fun toString(): String = "DynamicGroup(${joinToString(", ")})"
    }

    // Custom nodes
    // => File(nodes)
    data class File<T1 : Node<T1>>(
        val statements: Node<T1>,
    ) : Node<T1>()

    // => Statement(modifiers, name, nodes)
    data class Statement<T1 : Node<T1>, T2 : Node<T2>, T3 : Node<T3>>(
        val modifiers: Node<T1>,
        val name: Node<T2>,
        val nodes: Node<T3>,
    ) : Node<Statement<T1, T2, T3>>()

    // => Kleene(pattern, type = $enumStringMap(KleeneType, '*': STAR, '+': PLUS, '?': QUESTION)
    data class Kleene<T1 : Node<T1>>(
        val pattern: Node<T1>,
        val type: KleeneType,
    ) : Node<Kleene<T1>>() {
        companion object {
            fun <T1 : Node<T1>> `𝚌₁`(pattern: Node<T1>, `𝚜₁`: Node<*>) =
                Kleene(pattern, KleeneType.fromString(`𝚜₁`.`𝚟`))
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
