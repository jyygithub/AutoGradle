package com.jiangyy.autogradle.ui

import com.google.gson.Gson
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBList
import com.intellij.ui.content.ContentFactory
import com.jiangyy.autogradle.entity.ApiResponse
import com.jiangyy.autogradle.entity.Repository
import com.jiangyy.autogradle.ui.table.ToolWindowCellRenderer
import okhttp3.*
import java.io.IOException
import javax.swing.DefaultListModel
import javax.swing.JTextField
import javax.swing.ListSelectionModel
import javax.swing.border.EmptyBorder
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.BadLocationException

class EntranceToolWindow : ToolWindowFactory, DocumentListener {

    private var bindData = mutableListOf<Repository>()
    private var originalData = mutableListOf<Repository>()

    private lateinit var list: JBList<Repository>

    private val model = DefaultListModel<Repository>()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()

        listRepos()

        list = JBList(model)
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.cellRenderer = ToolWindowCellRenderer()

        val searchTextField = JTextField()
        searchTextField.document.addDocumentListener(this)
        val t = com.intellij.ui.layout.panel {
            row {
                searchTextField()
            }
            row {
                scrollPane(list).apply {
                    component.border = null
                }
            }
        }
        t.border = EmptyBorder(20, 20, 20, 20)
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
                    model.clear()
                    model.addAll(bindData)
                    list.updateUI()
                }

            }
        })
    }

    override fun insertUpdate(documentEvent: DocumentEvent?) {
        search(documentEvent)
    }

    override fun removeUpdate(documentEvent: DocumentEvent?) {
        search(documentEvent)
    }

    override fun changedUpdate(documentEvent: DocumentEvent?) {

    }

    private fun search(documentEvent: DocumentEvent?) {
        if (documentEvent == null) return
        val document = documentEvent.document
        try {
            val keyword = document.getText(0, document.length).lowercase()
            search(keyword)
        } catch (e: BadLocationException) {
            e.printStackTrace()
        }
    }

    private fun search(input: String?) {
        bindData.clear()
        when {
            input.isNullOrBlank() -> bindData.addAll(originalData)
            else -> {
                for (i in originalData.indices) {
                    val item = originalData[i]
                    if (item.nickname.orEmpty().lowercase().contains(input.orEmpty())) {
                        bindData.add(originalData[i])
                    }
                }
            }
        }
        list.updateUI()
    }

}