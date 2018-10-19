package com.ezappx.plugin.generator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.system.exitProcess

class Epg : CliktCommand() {
    private val currentDir: Path = Paths.get(System.getProperty("user.dir"))
    private val projectName: String by option("-n", "--name", help = "Plugin name").prompt("Input the plugin name").validate { require(it.isNotBlank()) }
    private val template: String by option("-t", "--template", help = "Template plugin path. Default path is ezappx-plugin-template").default("ezappx-plugin-template")
    private val localDeploymentScript by option("-lds", "--localDeploymentScript", help = "Generate local deployment script").flag(default = false)
    private val gitDeploymentScript by option("-gds", "--gitDeploymentScript", help = "Generate git deployment script").flag(default = false)
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
        val templateFiles: File = templateDir.toFile()
        if (templateFiles.exists()) {
            templateFiles.copyRecursively(projectDir.toFile(), overwrite = true)
            if (localDeploymentScript) modifyScriptAndCopy("deploy-local.sh")
            if (gitDeploymentScript) modifyScriptAndCopy("deploy-git.sh")
        } else {
            echo("Not a valid path: $templateDir")
        }
    }

    private fun modifyScriptAndCopy(scriptName: String) {
        var content = this.javaClass.getResourceAsStream("/$scriptName").bufferedReader().use { it.readText() }
        content = content.replace("ezappx-plugin-template", projectName)
        content = content.replace("local-ezappx-project-js-dir", ezappxPluginDir.toBashPath())
        projectDir.resolve(scriptName).toFile().writeText(content)
    }

    /**
     * Convert E:\JavaProjects\Ezappx to /E/JavaProjects/Ezappx
     */
    private fun String.toBashPath(): String {
        return "/" + this.replace("\\", "/").replace(":", "")
    }

    private fun modifyContent() {
        val modifiedFiles = arrayListOf("package.json", "index.html")
        projectDir.toFile().walkTopDown()
                .filter { it.name in modifiedFiles }
                .forEach { it.writeText(it.readText().replace("ezappx-plugin-template", projectName)) }
    }
}