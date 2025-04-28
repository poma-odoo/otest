package com.odoo.otest.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializer
import org.jdom.Element

class OTestConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
) : RunConfigurationBase<OTestConfiguration>(project, factory, name) {

    var databaseName: String = ""
    var modules: String = ""
    var testClasses: String = ""
    var testMethods: String = ""
    var testTags: String = ""
    var odooBinPath: String = ""

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return OTestConfigurationEditor()
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
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

/*
    @NlsSafe
    fun getDatabaseName(): String {
        return databaseName
    }

    @NlsSafe
    fun getModules(): String {
        return modules
    }

    @NlsSafe
    fun getTestClasses(): String {
        return testClasses
    }

    @NlsSafe
    fun getTestMethods(): String {
        return testMethods
    }

    @NlsSafe
    fun getTestTags(): String {
        return testTags
    }

    @NlsSafe
    fun getOdooBinPath(): String {
        return odooBinPath
    }
*/
}