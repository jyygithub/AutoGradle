package com.jiangyy.autogradle.ui

import com.google.gson.Gson
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBList
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.jiangyy.autogradle.entity.ApiResponse
import com.jiangyy.autogradle.entity.Repository
import com.jiangyy.autogradle.ui.table.ToolWindowModel
import com.jiangyy.autogradle.ui.table.ToolWindowCellRenderer
import okhttp3.*
import java.io.IOException
import javax.swing.border.EmptyBorder

class EntranceToolWIndow : ToolWindowFactory {

    private var bindData = mutableListOf<Repository>()
    private var originalData = mutableListOf<Repository>()

    private lateinit var list: JBList<Repository>

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()

        listRepos()

        list = JBList(ToolWindowModel(bindData))
        list.cellRenderer = ToolWindowCellRenderer()

        val t = panel {
            row {
                textField().horizontalAlign(HorizontalAlign.FILL)
            }
            row {
                cell(list).horizontalAlign(HorizontalAlign.FILL)
            }
        }
        t.border = EmptyBorder(5, 5, 5, 5)
        val content = contentFactory.createContent(t, "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun listRepos() {
        OkHttpClient().newCall(
                Request.Builder()
                        .addHeader("factory-api-version", "v2.0")
                        .url("https://plugins.95factory.com/api/autogradle/repository").get().build()
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                Gson().fromJson<ApiResponse>(response.body?.string(), ApiResponse::class.java)?.let { result ->
                    originalData = result.data
                    bindData.clear()
                    bindData.addAll(result.data)
                    list.updateUI()
                }

            }
        })
    }

}