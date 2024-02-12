@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package com.buttersus.gramutils

/**
 * Wrapper for a nullable value.
 * In this case, it represents `Empty` node.
 *
 * It's a replacement for `Optional` from Java.
 *
 * @param NB Node type.
 * @property value A value to wrap.
 */
class Opt<NB : NodeBase<NB>> private constructor() {
    private var value: NB? = null

    companion object {
        /**
         * An empty optional node.
         */
        val EMPTY = Opt<Nothing>()

        /**
         * Creates an optional node with empty value.
         * _(Uses `<Nothing>` as a type)_
         *
         * @return An empty optional node.
         */
        @Suppress("UNCHECKED_CAST")
        fun <NB : NodeBase<NB>> empty(): Opt<NB> = EMPTY as Opt<NB>

        /**
         * Avoid creating a new instance of `Opt` with `Opt` as a type.
         * It's useless, because it's the same as `Opt`.
         *
         * @param opt Optional node.
         * @return The same optional node.
         */
        fun <NB : NodeBase<NB>> of(opt: Opt<NB>): Opt<NB> = opt

        /**
         * Avoid creating a new instance of `Opt` with `Opt` as a type.
         * It's useless, because it's the same as `Opt`.
         *
         * @param opt Optional node.
         * @return The same optional node.
         */
        fun <NB : NodeBase<NB>> of(opt: Opt<NB>?): Opt<NB>? = opt

        /**
         * Creates an optional node with a value.
         * _(Uses `<NB>` as a type)_
         *
         * @param value A value to wrap.
         * @return An optional node with a value.
         */
        fun <NB : NodeBase<NB>> of(value: NB): Opt<NB> = Opt<NB>().apply { this.value = value }

        /**
         * Creates an optional node with a value.
         * _(Uses `<NB>` as a type)_
         *
         * @param value A nullable value to wrap.
         * @return A nullable optional node with a value.
         */
        fun <NB : NodeBase<NB>> of(value: NB?): Opt<NB>? = if (value == null) null else of(value)

        /**
         * Creates an optional node with a value.
         * If a value is `null`, then it creates an empty optional node.
         * _(Uses `<NB>` as a type)_
         *
         * @param value A nullable value to wrap.
         * @return An optional node with a value if it's not `null`, otherwise an empty optional node.
         */
        fun <NB : NodeBase<NB>> ofNullable(value: NB?): Opt<NB> = if (value == null) empty() else of(value)
    }

    /**
     * Checks if a value is empty.
     *
     * @return `true` if a value is empty, otherwise `false`.
     */
    fun isEmpty(): Boolean = value == null

    /**
     * Checks if a value is present.
     *
     * @return `true` if a value is present, otherwise `false`.
     */
    fun isPresent(): Boolean = value != null

    /**
     * Gets a value if it's present.
     *
     * @return A value if it's present.
     * @throws NoSuchElementException If a value is not present.
     */
    fun get(): NB = value ?: throw NoSuchElementException("No value present")

    /**
     * Gets a value if it's present, otherwise returns `other`.
     *
     * @param other Value to return if a value is not present.
     * @return A value if it's present, otherwise `other`.
     */
    fun orElse(other: NB): NB = value ?: other
}