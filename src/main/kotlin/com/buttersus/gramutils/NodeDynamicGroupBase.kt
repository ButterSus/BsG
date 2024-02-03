package com.buttersus.gramutils

/**
 * A node that represents sequence of nodes.
 * - Indexing starts from 1.
 * - It's not a failure even if size is 0.
 *
 * @param SC Super type. _(NodeBase<SC>)_
 * @param S Self type.
 * @return Dynamic group of nodes
 */
interface NodeDynamicGroupBase<SC: NodeBase<SC>, S: NodeDynamicGroupBase<SC, S>> : MutableList<SC> {
    /**
     * Shortcut to get only needed node from the dynamic group.
     *
     * Usage:
     * ```
     * val 𝚗₁ = DynamicGroup(𝚗₁, 𝚗₂, 𝚗₃).item(1)
     * ```
     *
     * @param 𝚒 Index of needed node _(starts from 1)_
     * @return Needed node
     */
    fun item(`𝚒`: Int): SC = this[`𝚒` - 1]
}