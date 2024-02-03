@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package com.buttersus.gramutils

import kotlin.reflect.*
import kotlin.reflect.full.*
import com.moriatsushi.cacheable.*

/**
 * Base class for all nodes.
 * It contains position, properties, and other useful methods.
 *
 * @param S Self type.
 * @property 𝚙ₛ Start position of the node. _(Computed)_
 * @property 𝚙ₑ End position of the node. _(Computed)_
 * @property 𝚟 String value of the node. _(Computed)_
 */
abstract class NodeBase<S: NodeBase<S>> {
    open val `𝚙ₛ`: Position? by lazy {
        this.getPropertiesList().firstOrNull { it.second != null }?.second?.`𝚙ₛ`
    }
    open val `𝚙ₑ`: Position? by lazy {
        this.getPropertiesList().lastOrNull { it.second != null }?.second?.`𝚙ₑ`
    }
    val `𝚟`: String
        get() {
            val `𝚙ₛ` = this.`𝚙ₛ`
            val `𝚙ₑ` = this.`𝚙ₑ`
            if (`𝚙ₛ` == null || `𝚙ₑ` == null) return ""
            return `𝚙ₛ`.`𝚂`.`𝜔`.substring(`𝚙ₛ`.`𝚒`, `𝚙ₑ`.`𝚒` + 1)
        }

    /**
     * Check if the node is empty.
     *
     * Usage:
     * ```
     * val node = Group(𝚗₁, 𝚗₂, 𝚗₃)
     * if (node.isNodeEmpty()) { … }
     * ```
     *
     * @return `true` if the node is empty, `false` otherwise.
     * @see isNodeNotEmpty
     */
    fun isNodeEmpty(): Boolean = this::class.isSubclassOf(NodeEmptyBase::class)

    /**
     * Check if the node is not empty.
     *
     * Usage:
     * ```
     * val node = Group(𝚗₁, 𝚗₂, 𝚗₃)
     * if (node.isNodeNotEmpty()) { … }
     * ```
     *
     * @return `true` if the node is not empty, `false` otherwise.
     * @see isNodeEmpty
     */
    fun isNodeNotEmpty(): Boolean = !isNodeEmpty()

    /**
     * Get all properties of the node as a map.
     * It's a lazy property, so it will be calculated only once.
     *
     * Usage:
     * ```
     * val node = Group(𝚗₁, 𝚗₂, 𝚗₃)
     * val properties = node.getPropertiesMap<Node>()
     * ```
     *
     * @return Map of properties
     * @see getPropertiesList
     */
    @Cacheable
    fun getPropertiesMap(): Map<String, S?> =
        @Suppress("UNCHECKED_CAST")
        this::class.declaredMemberProperties
            .filter { (it.returnType.classifier as KClass<*>).isSubclassOf(NodeBase::class) }
            .associate { it.name to it.getter.call(this) as S? }

    /**
     * Get all properties of the node as a list.
     * It's a lazy property, so it will be calculated only once.
     *
     * Usage:
     * ```
     * val node = Group(𝚗₁, 𝚗₂, 𝚗₃)
     * val properties = node.getPropertiesList<Node>()
     * ```
     *
     * @return List of properties
     * @see getPropertiesMap
     */
    @Cacheable
    fun getPropertiesList(): List<Pair<String, S?>> =
        @Suppress("UNCHECKED_CAST")
        this::class.declaredMemberProperties
            .filter { (it.returnType.classifier as KClass<*>).isSubclassOf(NodeBase::class) }
            .map { it.name to it.getter.call(this) as S? }

    /**
     * Get all parameters of the node as a map.
     * It's used for printing the node in a more readable way.
     *
     * Usage:
     * ```
     * val node = Group(𝚗₁, 𝚗₂, 𝚗₃)
     * val parameters = node.getParameters()
     * ```
     *
     * @return Map of parameters
     * @see getParametersList
     */
    open fun getParametersMap(): Map<String, Any> = mapOf()

    /**
     * Get all parameters of the node as a list.
     * It's used for printing the node in a more readable way.
     *
     * Usage:
     * ```
     * val node = Group(𝚗₁, 𝚗₂, 𝚗₃)
     * val parameters = node.getParameters()
     * ```
     *
     * @return List of parameters
     * @see getParametersMap
     */
    fun getParametersList(): List<Pair<String, Any?>> = this.getParametersMap().toList()
}