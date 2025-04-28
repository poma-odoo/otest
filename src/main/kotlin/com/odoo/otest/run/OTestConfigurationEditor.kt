package com.odoo.otest.run

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.openapi.fileChooser.FileChooserDescriptor
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.components.service
import com.intellij.openapi.project.ProjectManager
import com.intellij.util.ui.FormBuilder
import com.odoo.otest.OTestBundle
import javax.swing.JComponent
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener


class OTestConfigurationEditor (private val compiledTestTagsUpdater: () -> Unit): SettingsEditor<OTestConfiguration>() {
    private val databaseNameField = JBTextField()
    private val testModulesField = JBTextField()
    private val testClassesField = JBTextField()
    private val testMethodsField = JBTextField()
    private val testTagsField = JBTextField()
    private val odooBinPathField = TextFieldWithBrowseButton()
    private val compiledTestTagsField = JBTextField().apply { isEnabled = false }
    private var currentConfiguration: OTestConfiguration? = null


    init {
        val project = ProjectManager.getInstance().defaultProject // Get the default project
        val fileChooserDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
            .withFileFilter { it.name == "odoo-bin" || it.extension == "py" } // Suggest odoo-bin or Python files

        odooBinPathField.addBrowseFolderListener(
            "Select Odoo `odoo-bin`",
            null,
            project, // Pass the project here
            fileChooserDescriptor
        )
    }

    override fun resetEditorFrom(configuration: OTestConfiguration) {
        currentConfiguration = configuration
        databaseNameField.text = configuration.databaseName
        testModulesField.text = configuration.testModules
        testClassesField.text = configuration.testClasses
        testMethodsField.text = configuration.testMethods
        testTagsField.text = configuration.testTags
        odooBinPathField.text = configuration.odooBinPath
        compiledTestTagsField.text = configuration.compiledTestTags

        val documentListener = object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) { updateCompiledTestTagsPreview() }
            override fun removeUpdate(e: DocumentEvent?) { updateCompiledTestTagsPreview() }
            override fun changedUpdate(e: DocumentEvent?) { updateCompiledTestTagsPreview() }
        }
        testModulesField.document.addDocumentListener(documentListener)
        testClassesField.document.addDocumentListener(documentListener)
        testMethodsField.document.addDocumentListener(documentListener)
        testTagsField.document.addDocumentListener(documentListener)
    }

    override fun applyEditorTo(configuration: OTestConfiguration) {
        configuration.databaseName = databaseNameField.text
        configuration.testModules = testModulesField.text
        configuration.testClasses = testClassesField.text
        configuration.testMethods = testMethodsField.text
        configuration.testTags = testTagsField.text
        configuration.odooBinPath = odooBinPathField.text
        configuration.updateCompiledTestTags()

    }

    override fun createEditor(): JComponent {
        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.database")), databaseNameField)
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.testModules")).apply { toolTipText = OTestBundle.message("run.configuration.otest.testModules.tooltip")}, testModulesField)
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.testClasses")).apply { toolTipText = OTestBundle.message("run.configuration.otest.testClasses.tooltip")}, testClassesField)
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.testMethods")).apply { toolTipText = OTestBundle.message("run.configuration.otest.testMethods.tooltip")}, testMethodsField)
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.testTags")).apply { toolTipText = OTestBundle.message("run.configuration.otest.testTags.tooltip")}, testTagsField)
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.odooBinPath")), odooBinPathField)
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.compiledTestTags")), compiledTestTagsField)
            .panel
    }

    private fun updateCompiledTestTagsPreview() {
        currentConfiguration?.let { configuration ->
            configuration.databaseName = databaseNameField.text
            configuration.testModules = testModulesField.text
            configuration.testClasses = testClassesField.text
            configuration.testMethods = testMethodsField.text
            configuration.testTags = testTagsField.text
            configuration.odooBinPath = odooBinPathField.text
            configuration.updateCompiledTestTags()
            compiledTestTagsField.text = configuration.compiledTestTags
        }
    }
}