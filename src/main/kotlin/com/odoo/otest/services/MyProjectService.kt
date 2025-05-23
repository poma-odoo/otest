package com.odoo.otest.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.odoo.otest.OTestBundle

@Service(Service.Level.PROJECT)
class MyProjectService(project: Project) {

    init {
        thisLogger().info(OTestBundle.message("projectService", project.name))
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    fun getRandomNumber() = (1..100).random()
}
