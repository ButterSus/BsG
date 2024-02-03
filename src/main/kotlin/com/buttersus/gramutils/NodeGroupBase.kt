package com.buttersus.gramutils

/**
 * A node that represents static list of nodes.
 * - Indexing starts from 1.
 * - It's not a failure even if size is 0.
 *
 * @param SC Super type. _(NodeBase<SC>)_
 * @param S Self type.
 * @return Group of nodes
 */
interface NodeGroupBase<SC: NodeBase<SC>, S: NodeGroupBase<SC, S>> : List<SC> {
    /**
     * Creates a new group of nodes from the list of nodes.
     * It's used by the `select` method, so you must override it.
     */
    fun createGroup(nodes: List<SC>): S

    /**
     * Shortcut to get only needed nodes from the group.
     * _(returns only 20 components at most)_
     *
     * Usage:
     * ```
     * val (𝚗₁, 𝚗₃) = Group(𝚗₁, 𝚗₂, 𝚗₃).select(1, 3)
     * ```
     *
     * @param 𝚒s Indices of needed nodes _(starts from 1)_
     * @return List of needed nodes
     */
    fun select(vararg `𝚒s`: Int): S = createGroup(`𝚒s`.map { this[it - 1] })

    /**
     * Shortcut to get only needed node from the group.
     *
     * Usage:
     * ```
     * val 𝚗₁ = Group(𝚗₁, 𝚗₂, 𝚗₃).item(1)
     * ```
     *
     * @param 𝚒 Index of needed node _(starts from 1)_
     * @return Needed node
     */
    fun item(`𝚒`: Int): SC = this[`𝚒` - 1]

    // Destructuring
    operator fun component1(): SC = this[0]
    operator fun component2(): SC = this[1]
    operator fun component3(): SC = this[2]
    operator fun component4(): SC = this[3]
    operator fun component5(): SC = this[4]
    operator fun component6(): SC = this[5]
    operator fun component7(): SC = this[6]
    operator fun component8(): SC = this[7]
    operator fun component9(): SC = this[8]
    operator fun component10(): SC = this[9]
    operator fun component11(): SC = this[10]
    operator fun component12(): SC = this[11]
    operator fun component13(): SC = this[12]
    operator fun component14(): SC = this[13]
    operator fun component15(): SC = this[14]
    operator fun component16(): SC = this[15]
    operator fun component17(): SC = this[16]
    operator fun component18(): SC = this[17]
    operator fun component19(): SC = this[18]
    operator fun component20(): SC = this[19]
}