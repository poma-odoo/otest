package com.odoo.otest.run

import com.intellij.execution.Executor
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.filters.TextConsoleBuilder
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.openapi.util.Key
import org.jetbrains.annotations.NotNull
import java.nio.charset.Charset

class OTestRunState(environment: ExecutionEnvironment, private val configuration: OTestConfiguration) :
    CommandLineState(environment) {
    private var consoleView: ConsoleView? = null

    override fun startProcess(): OSProcessHandler {
        val commandLine = GeneralCommandLine(configuration.odooBinPath)
            .withCharset(Charset.forName("UTF-8"))
            .withWorkDirectory(environment.project.basePath)
//        todo: add odoo addons folder finder

        commandLine.addParameter("--test-enable")
        commandLine.addParameter("--test-tags=${configuration.compiledTestTags}")
        commandLine.addParameter("--database=${configuration.databaseName}")


        val handler = OSProcessHandler(commandLine)
        handler.addProcessListener(object : ProcessAdapter() {
            private var testsPassed = true

            override fun onTextAvailable(@NotNull event: ProcessEvent, @NotNull outputType: Key<*>) {
                val contentType = when (outputType) {
                    ProcessOutputTypes.STDOUT -> com.intellij.execution.ui.ConsoleViewContentType.NORMAL_OUTPUT
                    ProcessOutputTypes.STDERR -> com.intellij.execution.ui.ConsoleViewContentType.ERROR_OUTPUT
                    else -> com.intellij.execution.ui.ConsoleViewContentType.NORMAL_OUTPUT // Default
                }
                consoleView?.print(event.text, contentType)
                if (event.text.contains("FAILED") || event.text.contains("ERROR")) {
                    testsPassed = false
                }
            }

            override fun processTerminated(@NotNull event: ProcessEvent) {
                val resultContentType = if (testsPassed && event.exitCode == 0) {
                    com.intellij.execution.ui.ConsoleViewContentType.NORMAL_OUTPUT
                } else {
                    com.intellij.execution.ui.ConsoleViewContentType.ERROR_OUTPUT
                }
                consoleView?.print("\nOTest: ", com.intellij.execution.ui.ConsoleViewContentType.NORMAL_OUTPUT)
                consoleView?.print(
                    if (testsPassed && event.exitCode == 0) "PASSED\n" else "FAILED (exit code: ${event.exitCode})\n",
                    resultContentType
                )
            }
        })

        return handler
    }

    override fun createConsole(@NotNull executor: Executor): ConsoleView {
        if (consoleView == null) {
            val consoleBuilder: TextConsoleBuilder =
                TextConsoleBuilderFactory.getInstance().createBuilder(environment.project)
            consoleView = consoleBuilder.console
        }
        return consoleView!!
    }

}