package com.ezappx.plugin.generator

import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths

class PluginGenerator(val args: Array<String>) {
    private val cli = CommandLineBuilder()
    private val currentDir= Paths.get(System.getProperty("user.dir"))
    private val name = "name"
    private val deployScript = "deployScript"

    init { // init command line
        cli.desc = "ezappx 插件模板生成工具"
        cli.addCommand(name, "插件名称", "")
        cli.addCommand(deployScript, "生成部署脚本", false)
        generateProject()
    }

    private fun generateProject() {
        try {
            cli.parse(args)
        }catch (e:Exception) {
            cli.displayHelp()
            return
        }

        val projectName = cli.commands[name]?.value as String
        // validate the commands
        if (projectName.isBlank()) {
            cli.displayHelp()
            return
        }
        val templateProject = currentDir.resolve("ezappx-plugin-template").toFile()
        val newProjectDir = currentDir.resolve(projectName)
        if (Files.exists(newProjectDir)) {
            println("存在同名工程 $projectName, 是否覆盖 y/n ?")
            val opts = readLine()?.trim()?.toLowerCase() ?: "n"
            if (opts != "y") return
        }
        Files.createDirectories(newProjectDir)
        templateProject.copyRecursively(newProjectDir.toFile(), overwrite = true)
        modifierFileInfo(newProjectDir.toFile())
        println("生成 $projectName 插件工程")
    }

    private fun modifierFileInfo(projectDir: File) {
        val needChangeFileList = arrayListOf("package.json", "index.html", "deploy.sh")
        val pluginName = cli.commands[name]?.value as String
        projectDir.walkTopDown().filter { it.name in needChangeFileList }.forEach { changeContent(it, "ezappx-plugin-template", pluginName) }
    }

    private fun changeContent(file: File, oldContent: String, newContent: String) {
        val content = file.readText().replace(oldContent, newContent)
        file.writeText(content)
    }
}