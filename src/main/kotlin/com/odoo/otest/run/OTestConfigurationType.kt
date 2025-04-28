package com.odoo.otest.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.icons.AllIcons
import com.odoo.otest.OTestBundle
import javax.swing.Icon

class OTestConfigurationType : ConfigurationType {
    override fun getDisplayName(): String {
        return OTestBundle.message("run.configuration.otest.name")
    }

    override fun getConfigurationTypeDescription(): String {
        return OTestBundle.message("run.configuration.otest.description")
    }

    override fun getIcon(): Icon {
        return AllIcons.RunConfigurations.TestState.Run
    }

    override fun getId(): String {
        return "OTestRunConfiguration"
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf<ConfigurationFactory>(OTestConfigurationFactory(this)) // Explicitly specify the type
    }
}