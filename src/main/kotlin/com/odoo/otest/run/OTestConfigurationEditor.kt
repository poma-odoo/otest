package com.odoo.otest.run

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.openapi.fileChooser.FileChooserDescriptor
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.components.service
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.EditorTextField
import com.intellij.util.ui.FormBuilder
import com.odoo.otest.OTestBundle
import javax.swing.JComponent

class OTestConfigurationEditor : SettingsEditor<OTestConfiguration>() {
    private val databaseNameField = JBTextField()
    private val modulesField = JBTextField()
    private val testClassesField = JBTextField()
    private val testMethodsField = JBTextField()
    private val testTagsField = EditorTextField()
    private val odooBinPathField = TextFieldWithBrowseButton()

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
        databaseNameField.text = configuration.databaseName
        modulesField.text = configuration.modules
        testClassesField.text = configuration.testClasses
        testMethodsField.text = configuration.testMethods
        testTagsField.text = configuration.testTags
        odooBinPathField.text = configuration.odooBinPath
    }

    override fun applyEditorTo(configuration: OTestConfiguration) {
        configuration.databaseName = databaseNameField.text
        configuration.modules = modulesField.text
        configuration.testClasses = testClassesField.text
        configuration.testMethods = testMethodsField.text
        configuration.testTags = testTagsField.text
        configuration.odooBinPath = odooBinPathField.text
    }

    override fun createEditor(): JComponent {
        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.database")), databaseNameField)
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.modules")), modulesField)
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.testClasses")), testClassesField)
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.testMethods")), testMethodsField)
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.testTags")), testTagsField)
            .addLabeledComponent(JBLabel(OTestBundle.message("run.configuration.otest.odooBinPath")), odooBinPathField)
            .panel
    }
}