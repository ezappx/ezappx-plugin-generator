package com.ezappx.plugin.generator

interface CommandArg {
    val commandName: String
    val usage: String
    val value: Any
}

data class CommandString(override val commandName: String,
                         override val usage: String,
                         override var value: String) : CommandArg

data class CommandBoolean(override val commandName: String,
                          override val usage: String,
                          override var value: Boolean) : CommandArg
