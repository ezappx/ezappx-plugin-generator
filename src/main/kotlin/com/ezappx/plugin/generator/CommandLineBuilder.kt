package com.ezappx.plugin.generator

class CommandLineBuilder {
    val commands = mutableMapOf<String, CommandArg>()
    var desc: String = ""

    fun addCommand(name: String, usage: String, defaultValue: String) {
        commands[name] = CommandString(name, usage, defaultValue)
    }

    fun addCommand(name: String, usage: String, defaultValue: Boolean) {
        commands[name] = CommandBoolean(name, usage, defaultValue)
    }

    fun displayHelp() {
        println(desc)
        commands.forEach { println("-${it.value.commandName}: ${it.value.usage}") }
    }

    fun parse(args: Array<String>) {
        for (i in args.indices) {
            if (args[i].startsWith("-") && commands.containsKey(args[i].removePrefix("-"))) { // is -test
                val name = args[i].removePrefix("-")
                if (i + 1 == args.size || args[i + 1].startsWith("-")) { // last arg without value or boolean arg
                    val command = commands[name] as CommandBoolean
                    command.value = true
                } else { // string arg
                    val command = commands[name] as CommandString
                    command.value = args[i + 1]
                }
            }
        }
    }
}