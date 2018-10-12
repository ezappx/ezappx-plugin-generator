package com.ezappx.plugin.generator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.system.exitProcess

class Epg : CliktCommand() {
    private val currentDir: Path = Paths.get(System.getProperty("user.dir"))
    private val projectName: String by option("-n", "--name", help = "Plugin name").prompt("Input the plugin name").validate { require(it.isNotBlank()) }
    private val template: String by option("-t", "--template", help = "Template plugin path. Default path is ezappx-plugin-template").default("ezappx-plugin-template").validate { require(Files.exists(currentDir.resolve(it))) }
    private val deployScript by option("-ds", "--deployScript", help = "Generate deployment script").flag(default = false)
    private val gitDeployScript by option("-gds", "--gitDeployScript", help = "Generate git and deployment script").flag(default = false)
    private val ezappxPluginDir: String by option("-epd", "--ezappxPluginDir", help = "Ezappx plugin path").default("")

    private lateinit var templateDir: Path
    private lateinit var projectDir: Path

    override fun run() {
        templateDir = currentDir.resolve(template)
        projectDir = currentDir.resolve(projectName)
        prepareProjectDir()
        copyTemplate2Dir()
        modifyContent()
    }

    private fun prepareProjectDir() {
        if (Files.exists(projectDir)) {
            echo("Override the project $projectName y/n?")
            val overrideDir = readLine()?.trim()?.toLowerCase() ?: "n"
            if (overrideDir != "y") exitProcess(0)
        } else {
            Files.createDirectories(projectDir)
        }
    }

    private fun copyTemplate2Dir() {
        if (deployScript) modifyScriptAndCopy("deploy.sh")
        if (gitDeployScript) modifyScriptAndCopy("gitDeploy.sh")
        templateDir.toFile().copyRecursively(projectDir.toFile(), overwrite = true)
    }

    private fun modifyScriptAndCopy(scriptName: String) {
        var content = this.javaClass.getResourceAsStream("/$scriptName").bufferedReader().use { it.readText() }
        content = content.replace("plugin-name", projectName)
        content = content.replace("ezappx-plugin-dir", ezappxPluginDir.replace("\\", "/"))
        projectDir.resolve(scriptName).toFile().writeText(content)
    }

    private fun modifyContent() {
        val modifiedFiles = arrayListOf("package.json", "index.html")
        projectDir.toFile().walkTopDown()
                .filter { it.name in modifiedFiles }
                .forEach { it.writeText(it.readText().replace("ezappx-plugin-template", projectName)) }
    }
}