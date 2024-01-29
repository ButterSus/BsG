package com.buttersus

import com.buttersus.blg.*
import mu.KotlinLogging

val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Reading file..." }
    val `𝚂` = Lexer::class.java.getResource("/main.blg")
        ?.readText()?.toSource() ?: throw Exception("Could not read file")
    logger.info { "File read" }

    val `𝕃` = Lexer()(`𝚂`).tokenize()
    val `ℙ` = Parser()(`𝕃`).parse()

    logger.info { "Printing..." }
    println(`ℙ`?.toPretty())
    logger.info { "Printed" }
}
