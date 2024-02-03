package com.buttersus.gramutils

/**
 * A classic wrapper for any token.
 * It's easier to use tokens when they are wrapped.
 *
 * Usage:
 * ```
 * val token = Token(…)
 * val wrapper = Wrapper(token)
 * println(wrapper) // Wrapper(NUMBER(32))
 * ```
 *
 * @param TT Type of the token
 * @param TB Token type
 * @property 𝚝 Token to be wrapped
 * @return Wrapper of the token
 *
 * @see TokenBase
 */
interface NodeWrapperBase<TT: TypeBase, TB: TokenBase<TT>> {
    val `𝚝`: TB
}
