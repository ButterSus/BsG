@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package com.buttersus.gramutils

import mu.KotlinLogging
import org.w3c.dom.Node

/**
 * Base class for all parsers.
 *
 * This parser has a few ideas:
 * - Memoization for all productions
 * - Left recursion support
 * - PEG parsing
 * - Each production, which returns null, does not consume the token
 *
 * @param S Self type.
 * @param NB Node type.
 * @param WB Wrapper type.
 * @param LB Lexer type.
 * @param TT Token type enum.
 * @param TB Token type.
 * @property 𝕃 Lexer to parse.
 * @property 𝕋 Token list, which dynamically grows.
 *
 * @constructor Creates a parser with the given logging
 */
@Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")
abstract class ParserBase
<
        S : ParserBase<S, NB, WB, LB, TT, TB>, NB : NodeBase<NB>, WB,
        LB : LexerBase<LB, TT, TB>, TT : TypeBase, TB : TokenBase<TT>
        > where WB : NodeWrapperBase<TT, TB>, WB: NodeBase<WB>, WB: NB {
    // Necessary create methods
    /**
     * Creates a wrapper for the token.
     * It's used by the `≡` method, so you must override it.
     */
    protected abstract fun createWrapperNode(`𝚝`: TB): WB

    /**
     * Creates a group of nodes from the list of nodes.
     * It's used by the `∨` method, so you must override it.
     */
    protected abstract fun <N : NodeBase<N>> createGroupNode(nodes: List<Opt<N>>): NodeGroupBase<N, *>

    /**
     * Creates a group of nodes from the list of nodes.
     * It's used by the `∨` method, so you must override it.
     */
    protected abstract fun <N : NodeBase<N>> createDynamicGroupNode(nodes: List<Opt<NB>>): NodeDynamicGroupBase<N, *>

    /**
     * Creates a syntax exception.
     * It's used by the `!` method, so you must override it.
     */
    protected abstract fun raiseSyntaxException(`𝚙ₛ`: Position, `𝚙ₑ`: Position, `𝚝`: String): Nothing

    // Logging
    protected var logger = KotlinLogging.logger {}

    // Tokenization
    protected lateinit var `𝕃`: LB
    protected val `𝕋`: ArrayList<TB> = ArrayList()
    private var `𝚒` = 0
    private lateinit var `𝕀`: Iterator<TB>

    // Parsing
    /**
     * Parses the given lexer and returns the root node of the AST.
     *
     * @return Root node of the AST.
     */
    abstract fun <N> parse(): Opt<N>? where N: NodeBase<N>, N: NB

    /**
     * Resets the parser to its initial state, and sets the lexer to the given one.
     * _(Useful for reusing the parser.)_
     *
     * @param 𝕃 Lexer to parse.
     * @return This parser.
     */
    operator fun invoke(`𝕃`: LB): S {
        this.`𝕃` = `𝕃`
        this.`𝕀` = `𝕃`.iterator()
        this.`𝕋`.clear()
        this.`𝕄`.clear()
        @Suppress("UNCHECKED_CAST")
        return this as S
    }

    // Memoization
    private val `𝕄`: MutableMap<Index, MutableMap<String, Pair<Opt<NB>?, Index>>> = mutableMapOf()
    private val traceStack = mutableListOf<String>()

    /**
     * Memoization method for `𝚏` productions,
     * which greatly improves the time complexity: `O(n²)` -> `≈O(n)`
     *
     * Usage:
     * ```
     * // method of Parser: ↓
     * fun parseNode(): Node? = `𝚖`(…)
     * ```
     *
     * @param name Name of the production. _(Used as a key in the memoization table.)_
     * @param recursive Whether the production is left-recursive or not.
     * @param 𝚏 Production to memoize.
     * @return The result of the production.
     *
     * @see 𝕄
     */
    protected fun <N : NB, T : Opt<N>> `𝚖`(name: String, recursive: Boolean = false, `𝚏`: () -> T?): T? {
        logger.trace {
            val last = traceStack.lastOrNull()
            "+${traceStack.size + 1}($name)".padEnd(21) + " | ${last ?: "∅"}".also { traceStack.add(name) }
        }

        fun onReturn(it: T?) = logger.trace {
            traceStack.removeLast()
            " -${traceStack.size + 1}($name)".padEnd(21) + " -> $it"
        }

        val `𝚒₀` = `𝚒`
        val `𝚖₀` = `𝕄`.getOrPut(`𝚒₀`) { mutableMapOf() }
        `𝚖₀`[name]?.run {
            reset(second)
            @Suppress("UNCHECKED_CAST")
            return (first as T?).also(::onReturn)
        }
        @Suppress("UNCHECKED_CAST")
        if (!recursive) return `𝚏`().also { `𝚖₀`[name] = it as Opt<NB>? to mark() }.also(::onReturn)
        var `𝚗`: T? = null
        var `𝚒`: Index = `𝚒₀`
        `𝚖₀`[name] = null to `𝚒`
        while (true) {
            reset(`𝚒₀`)
            val `𝚗′` = `𝚏`()
            if (mark() <= `𝚒`) break
            `𝚗` = `𝚗′`
            `𝚒` = mark()
            @Suppress("UNCHECKED_CAST")
            `𝚖₀`[name] = (`𝚗` as Opt<NB>?) to `𝚒`
        }
        return `𝚗`.also { reset(`𝚒`) }.also(::onReturn)
    }

    // Internal parsing
    /**
     * Returns the current index of the token list.
     * It's not recommended to use it directly, only for really specific cases.
     */
    protected fun mark(): Index = `𝚒`

    /**
     * Resets the current index of the token list.
     * It's not recommended to use it directly, only for really specific cases.
     */
    protected fun reset(`𝚒₀`: Index) {
        `𝚒` = `𝚒₀`
    }

    /**
     * Returns the next token from the list, or `null` if there are no more tokens.
     * It's not recommended to use it directly, only for really specific cases.
     */
    protected fun peek(): TB? {
        while (`𝕋`.size <= `𝚒`) if (`𝕀`.hasNext()) `𝕋`.add(`𝕀`.next()) else return null
        return `𝕋`[`𝚒`]
    }

    /**
     * Returns and consumes the next token from the list, or `null` if there are no more tokens.
     * It's not recommended to use it directly, only for really specific cases.
     */
    protected fun next(): TB? = peek()?.also { `𝚒`++ }

    // Public parsing
    /**
     * Wraps the given function, and resets the parser if the function failed.
     * It's highly recommended to use it for all external methods that parse something.
     */
    protected fun <N : NB, T : Opt<N>> (() -> T?).withReset(): () -> T? = {
        val `𝚒` = mark()
        this() ?: null.also { reset(`𝚒`) }
    }

    /** Match by string. */
    protected open fun `≡`(`𝚟`: String): Opt<WB>? {
        val `𝚟′` = peek()?.`𝚟` ?: return null
        return if (`𝚟′` == `𝚟`) next()?.let { Opt.of(createWrapperNode(it)) } ?: return null else null
    }

    /** Match by type. */
    protected open fun `≈`(`𝚝`: TT): Opt<WB>? {
        val `𝚝′` = peek()?.`𝚃` ?: return null
        return if (`𝚝′` == `𝚝`) next()?.let { Opt.of(createWrapperNode(it)) } ?: return null else null
    }

    /** Lookahead. */
    protected fun <N : NB, T : Opt<N>> `≟`(`𝚏`: () -> T?): T? {
        val `𝚒` = mark()
        return `𝚏`()?.also { reset(`𝚒`) }
    }

    /** Negative lookahead. */
    protected fun <N : NB, T : Opt<N>> `≠`(`𝚏`: () -> T?): T? {
        val `𝚒` = mark()
        @Suppress("UNCHECKED_CAST")
        return if (`𝚏`() == null) Opt.EMPTY as T? else null.also { reset(`𝚒`) }
    }

    /** Optional. */
    @Suppress("UNCHECKED_CAST")
    protected fun <N : NB, T : Opt<N>> `∅`(`𝚏`: () -> T?): T = `𝚏`() ?: Opt.EMPTY as T

    /** Alternative. */
    protected fun <N : NB, T : Opt<N>> `∨`(`𝚏s`: List<() -> T?>): T? = `𝚏s`.firstNotNullOfOrNull { it() }

    /** One or more. */
    protected fun <N : NB, TG : NodeDynamicGroupBase<N, TG>> `⊕`(`𝚏`: () -> Opt<N>?): TG? {
        val `ℕ` = createDynamicGroupNode<N, TG>(listOf(`𝚏`() ?: return null))
        while (true) `𝚏`()?.also { `ℕ`.add(it) } ?: return `ℕ`
    }

    /** Zero or more. */
    protected fun <N : NB, T : Opt<N>, TG : NodeDynamicGroupBase<N, T, TG>> `⊛`(`𝚏`: () -> T?): TG {
        val `ℕ` = createDynamicGroupNode<N, T, TG>(listOf())
        while (true) `𝚏`()?.also { `ℕ`.add(it) } ?: return `ℕ`
    }

    /** One or more separated by. */
    protected fun <N : NB, T : Opt<N>, TG : NodeDynamicGroupBase<N, T, TG>> `⊕̂`(`𝚏`: () -> T?, `𝚜`: () -> T?): TG? {
        val `ℕ` = createDynamicGroupNode<N, T, TG>(listOf(`𝚏`() ?: return null))
        while (true) {
            val `𝚒` = mark(); `𝚜`() ?: return `ℕ`
            `𝚏`()?.also { `ℕ`.add(it) } ?: return `ℕ`.also { reset(`𝚒`) }
        }
    }

    /** Zero or more separated by. */
    protected fun <N : NB, T : Opt<N>, TG : NodeDynamicGroupBase<N, T, TG>> `⊛̂`(`𝚏`: () -> T?, `𝚜`: () -> T?): TG {
        var `𝚒` = mark()
        val `ℕ` = createDynamicGroupNode<N, T, TG>(listOf())
        while (true) {
            `𝚏`()?.also { `ℕ`.add(it) } ?: return `ℕ`.also { reset(`𝚒`) }
            `𝚒` = mark()
            `𝚜`() ?: return `ℕ`
        }
    }

    /** Group. */
    protected fun <N : NB, T : Opt<N>, TS : NodeGroupBase<N, T, TS>> `{…}`(`𝚏s`: List<() -> T?>): TS? {
        val `𝚒` = mark()
        return createGroupNode(`𝚏s`.map { it() ?: return null.also { reset(`𝚒`) } })
    }

    /** Optional group. */
    protected fun <N : NB, T : Opt<N>, TS : NodeGroupBase<N, T, TS>> `{∅}`(`𝚏s`: List<Pair<() -> T?, T>>): TS {
        val `𝚒` = mark()
        return createGroupNode(`𝚏s`.map { (𝚏, _) ->
            𝚏() ?: return createGroupNode<N, T, TS>(`𝚏s`.map { it.second }).also { reset(`𝚒`) }
        })
    }

    /** Optional group. */
    protected fun <N : NB, T : Opt<N>, TS : NodeGroupBase<N, T, TS>> `{∅→}`(`𝚏s`: List<Pair<() -> T?, () -> T>>): TS {
        val `𝚒` = mark()
        return createGroupNode(`𝚏s`.map { (𝚏, _) ->
            𝚏() ?: return createGroupNode<N, T, TS>(`𝚏s`.map { it.second() }).also { reset(`𝚒`) }
        })
    }

    /** Forbidden. */
    protected fun <N : NB, T : Opt<N>> `!`(`𝚝`: String, `𝚏`: () -> T?): T = `𝚏`() ?: run {
        raiseSyntaxException(peek()?.`𝚙ₛ` ?: `𝕋`.last().`𝚙ₑ`, peek()?.`𝚙ₑ` ?: `𝕋`.last().`𝚙ₑ`, `𝚝`)
    }

    // Shortcut parsing
    @Suppress("UNCHECKED_CAST")
    protected fun <N : NB, T : Opt<N>> `≈∅`(`𝚝`: TT): T = `∅` { `≈`(`𝚝`) as T? }

    @Suppress("UNCHECKED_CAST")
    protected fun <N : NB, TG : NodeDynamicGroupBase<N, TG>> `≈⊕`(`𝚝`: TT): TG? =
        `⊕`<N, T, TG> { `≈`(`𝚝`) as T? }

    @Suppress("UNCHECKED_CAST")
    protected fun <N : NB, TG : NodeDynamicGroupBase<N, TG>> `≈⊛`(`𝚝`: TT): TG = `⊛` { `≈`(`𝚝`) as T? }

    protected fun <N : NB, T : Opt<N>> `≡∅`(`𝚟`: String): T = `∅` { `≡`(`𝚟`) }
    protected fun <N : NB, TG : NodeDynamicGroupBase<N, TG>> `≡⊕`(`𝚟`: String): TG? =
        `⊕`<N, TG> { `≡`(`𝚟`) }

    protected fun <N : NB, T : Opt<N>, TG : NodeDynamicGroupBase<N, T, TG>> `≡⊛`(`𝚟`: String): TG = `⊛` { `≡`(`𝚟`) }
    protected fun <N : NB, T : Opt<N>> `∨∅`(`𝚏s`: List<() -> T?>): T? = `∨`(`𝚏s`)
    protected fun <N : NB, T : Opt<N>, TG : NodeDynamicGroupBase<N, T, TG>> `∨⊕`(`𝚏s`: List<() -> T?>): TG? =
        `⊕`<N, T, TG> { `∨`(`𝚏s`) }

    protected fun <N : NB, T : Opt<N>, TG : NodeDynamicGroupBase<N, T, TG>> `∨⊛`(`𝚏s`: List<() -> T?>): TG = `⊛` { `∨`(`𝚏s`) }
}