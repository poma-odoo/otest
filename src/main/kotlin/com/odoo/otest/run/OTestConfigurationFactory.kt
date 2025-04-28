package com.odoo.otest.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.openapi.project.Project
import com.odoo.otest.OTestBundle

class OTestConfigurationFactory(configurationType: ConfigurationType) : ConfigurationFactory(configurationType) {
    override fun getName(): String {
        return OTestBundle.message("run.configuration.otest.name")
    }

    override fun createTemplateConfiguration(project: Project): OTestConfiguration {
        println("createTemplateConfiguration running")
        return OTestConfiguration(project, this, OTestBundle.message("run.configuration.otest.case_name"))
    }

    override fun getId(): String {
        return "OTestRunConfiguration"
    }
}