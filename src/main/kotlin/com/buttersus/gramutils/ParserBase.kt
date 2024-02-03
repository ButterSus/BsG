@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package com.buttersus.gramutils

import mu.KotlinLogging

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
 * @param EB Empty type.
 * @param SE Syntax exception type.
 * @param GB Group type.
 * @param DG Dynamic group type.
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
        S : ParserBase<S, NB, WB, EB, SE, GB, DG, LB, TT, TB>, NB : NodeBase<NB>, WB, EB,
        SE : SyntaxExceptionBase, GB, DG, LB : LexerBase<LB, TT, TB>, TT : TypeBase, TB : TokenBase<TT>,
        > where DG : NB, DG : NodeDynamicGroupBase<NB, DG>, GB : NB, GB : NodeGroupBase<NB, GB>,
                WB : NB, WB : NodeWrapperBase<TT, TB>, EB : NB, EB : NodeEmptyBase {
    // Necessary create methods
    /**
     * Creates a wrapper for the token.
     * It's used by the `≡` method, so you must override it.
     */
    protected abstract fun createWrapperNode(`𝚝`: TB): WB

    /**
     * Creates an empty node.
     * It's used by the `≠` method, so you must override it.
     */
    protected abstract fun createEmptyNode(): EB

    /**
     * Creates a group of nodes from the list of nodes.
     * It's used by the `∨` method, so you must override it.
     */
    protected abstract fun createGroupNode(nodes: List<NB>): GB

    /**
     * Creates a group of nodes from the list of nodes.
     * It's used by the `∨` method, so you must override it.
     */
    protected abstract fun createDynamicGroupNode(nodes: List<NB>): DG

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
    abstract fun parse(): NB?

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
    private val `𝕄`: MutableMap<Index, MutableMap<String, Pair<NB?, Index>>> = mutableMapOf()
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
    protected fun `𝚖`(name: String, recursive: Boolean = false, `𝚏`: () -> NB?): NB? {
        logger.trace {
            val last = traceStack.lastOrNull()
            "+${traceStack.size + 1}($name)".padEnd(21) + " | ${last ?: "∅"}".also { traceStack.add(name) }
        }

        fun onReturn(it: NB?) = logger.trace {
            traceStack.removeLast()
            " -${traceStack.size + 1}($name)".padEnd(21) + " -> $it"
        }

        val `𝚒₀` = `𝚒`
        val `𝚖₀` = `𝕄`.getOrPut(`𝚒₀`) { mutableMapOf() }
        `𝚖₀`[name]?.run { reset(second); return first.also(::onReturn) }
        if (!recursive) return `𝚏`().also { `𝚖₀`[name] = it to mark() }.also(::onReturn)
        var `𝚗`: NB? = null
        var `𝚒`: Index = `𝚒₀`
        `𝚖₀`[name] = null to `𝚒`
        while (true) {
            reset(`𝚒₀`)
            val `𝚗′` = `𝚏`()
            if (mark() <= `𝚒`) break
            `𝚗` = `𝚗′`
            `𝚒` = mark()
            `𝚖₀`[name] = `𝚗` to `𝚒`
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
    protected fun <R : NB, F : () -> R?> F.withReset(): () -> R? = {
        val `𝚒` = mark()
        this() ?: null.also { reset(`𝚒`) }
    }

    /** Match by string. */
    protected fun `≡`(`𝚟`: String): WB? {
        val `𝚟′` = peek()?.`𝚟` ?: return null
        return if (`𝚟′` == `𝚟`) next()?.let(::createWrapperNode) ?: return null else null
    }

    /** Match by type. */
    protected fun `≈`(`𝚝`: TT): WB? {
        val `𝚝′` = peek()?.`𝚃` ?: return null
        return if (`𝚝′` == `𝚝`) next()?.let(::createWrapperNode) ?: return null else null
    }

    /** Lookahead. */
    protected fun `≟`(`𝚏`: () -> NB?): NB? {
        val `𝚒` = mark()
        return `𝚏`()?.also { reset(`𝚒`) }
    }

    /** Negative lookahead. */
    protected fun `≠`(`𝚏`: () -> NB?): EB? {
        val `𝚒` = mark()
        return if (`𝚏`() == null) createEmptyNode() else null.also { reset(`𝚒`) }
    }

    /** Optional. */
    protected fun `∅`(`𝚏`: () -> NB?): NB = `𝚏`() ?: createEmptyNode()

    /** Alternative. */
    protected fun `∨`(`𝚏s`: List<() -> NB?>): NB? = `𝚏s`.firstNotNullOfOrNull { it() }

    /** One or more. */
    protected fun `⊕`(`𝚏`: () -> NB?): DG? {
        val `ℕ` = createDynamicGroupNode(listOf(`𝚏`() ?: return null))
        while (true) `𝚏`()?.also { `ℕ`.add(it) } ?: return `ℕ`
    }

    /** Zero or more. */
    protected fun `⊛`(`𝚏`: () -> NB?): DG {
        val `ℕ` = createDynamicGroupNode(listOf())
        while (true) `𝚏`()?.also { `ℕ`.add(it) } ?: return `ℕ`
    }

    /** One or more separated by. */
    protected fun `⊕̂`(`𝚏`: () -> NB?, `𝚜`: () -> NB?): DG? {
        val `ℕ` = createDynamicGroupNode(listOf(`𝚏`() ?: return null))
        while (true) {
            val `𝚒` = mark(); `𝚜`() ?: return `ℕ`
            `𝚏`()?.also { `ℕ`.add(it) } ?: return `ℕ`.also { reset(`𝚒`) }
        }
    }

    /** Zero or more separated by. */
    protected fun `⊛̂`(`𝚏`: () -> NB?, `𝚜`: () -> NB?): DG {
        var `𝚒` = mark()
        val `ℕ` = createDynamicGroupNode(listOf())
        while (true) {
            `𝚏`()?.also { `ℕ`.add(it) } ?: return `ℕ`.also { reset(`𝚒`) }
            `𝚒` = mark()
            `𝚜`() ?: return `ℕ`
        }
    }

    /** Group. */
    protected fun `{…}`(`𝚏s`: List<() -> NB?>): GB? {
        val `𝚒` = mark()
        return createGroupNode(`𝚏s`.map { it() ?: return null.also { reset(`𝚒`) } })
    }

    /** Optional group. */
    protected fun `{∅}`(`𝚏s`: List<Pair<() -> NB?, NB>>): GB {
        val `𝚒` = mark()
        return createGroupNode(`𝚏s`.map { (𝚏, _) ->
            𝚏() ?: return createGroupNode(`𝚏s`.map { it.second }).also { reset(`𝚒`) }
        })
    }

    /** Optional group. */
    protected fun `{∅→}`(`𝚏s`: List<Pair<() -> NB?, () -> NB>>): GB {
        val `𝚒` = mark()
        return createGroupNode(`𝚏s`.map { (𝚏, _) ->
            𝚏() ?: return createGroupNode(`𝚏s`.map { it.second() }).also { reset(`𝚒`) }
        })
    }

    /** Forbidden. */
    protected fun `!`(`𝚝`: String, `𝚏`: () -> NB?): NB = `𝚏`() ?: run {
        raiseSyntaxException(peek()?.`𝚙ₛ` ?: `𝕋`.last().`𝚙ₑ`, peek()?.`𝚙ₑ` ?: `𝕋`.last().`𝚙ₑ`, `𝚝`)
    }

    // Shortcut parsing
    protected fun `≈∅`(`𝚝`: TT): NB = `∅` { `≈`(`𝚝`) }
    protected fun `≈⊕`(`𝚝`: TT): DG? = `⊕` { `≈`(`𝚝`) }
    protected fun `≈⊛`(`𝚝`: TT): DG = `⊛` { `≈`(`𝚝`) }
    protected fun `≡∅`(`𝚟`: String): NB = `∅` { `≡`(`𝚟`) }
    protected fun `≡⊕`(`𝚟`: String): DG? = `⊕` { `≡`(`𝚟`) }
    protected fun `≡⊛`(`𝚟`: String): DG = `⊛` { `≡`(`𝚟`) }
    protected fun `∨∅`(`𝚏s`: List<() -> NB?>): NB? = `∨`(`𝚏s`)
    protected fun `∨⊕`(`𝚏s`: List<() -> NB?>): DG? = `⊕` { `∨`(`𝚏s`) }
    protected fun `∨⊛`(`𝚏s`: List<() -> NB?>): DG = `⊛` { `∨`(`𝚏s`) }
}