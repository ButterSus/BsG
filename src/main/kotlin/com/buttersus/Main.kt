package com.buttersus

import com.buttersus.blg.*
import com.buttersus.gramutils.*
import mu.KotlinLogging

val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Reading file..." }
    val `𝚂` = Lexer::class.java.getResource("/main.blg")
        ?.readText()?.toSource() ?: throw Exception("Could not read file")
    logger.info { "File read" }

    val `𝕃` = Lexer()(`𝚂`)
    val `ℙ` = Parser()(`𝕃`).parse()

    logger.info { "Printing..." }
    println(`ℙ`.prettier(2))
    logger.info { "Printed" }
}
