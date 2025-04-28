package com.odoo.otest.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.util.xmlb.XmlSerializer
import org.jdom.Element

class OTestConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<OTestConfiguration>(project, factory, name) {

    var databaseName: String = ""
    var testModules: String = ""
    var testClasses: String = ""
    var testMethods: String = ""
    var testTags: String = ""
    var odooBinPath: String = ""
    var compiledTestTags: String = ""

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return OTestConfigurationEditor(this::updateCompiledTestTags)
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        updateCompiledTestTags()
        return OTestRunState(environment, this)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        XmlSerializer.deserializeInto(this, element)
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        XmlSerializer.serializeInto(this, element)
    }

    fun updateCompiledTestTags() {
        compiledTestTags = compileTestTags()
    }

    @NlsSafe
    fun compileTestTags(): String {
        fun normalizeTags(input: String, startChar: Char): String {
            return input.replace("\\s".toRegex(), ",")
                .split(",")
                .filter { it.isNotEmpty() }
                .joinToString(separator = ",") { if (it.first().isLetter()) "$startChar$it" else it }
        }
        return listOfNotNull(
            testTags.takeIf { it.isNotEmpty() },
            normalizeTags(testModules, '/'),
            normalizeTags(testClasses, ':'),
            normalizeTags(testMethods, '.'),
        ).joinToString(separator = ",")
    }
}