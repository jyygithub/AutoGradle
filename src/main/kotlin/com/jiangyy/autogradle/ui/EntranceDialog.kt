package com.jiangyy.autogradle.ui

import com.google.gson.Gson
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.JBDimension
import com.jiangyy.autogradle.entity.ApiResponse
import com.jiangyy.autogradle.entity.Repository
import com.jiangyy.autogradle.ui.table.HomeTableModel
import com.jiangyy.autogradle.ui.table.IconColumn
import okhttp3.*
import org.jetbrains.annotations.Nullable
import java.awt.Desktop
import java.awt.Dimension
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.IOException
import java.net.URI
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.BadLocationException

private enum class ChoseModuleType {
    Kotlin, Groovy
}

class EntranceDialog(@Nullable private val event: AnActionEvent) : DialogWrapper(true), DocumentListener,
    MouseListener {

    private var _data = emptyList<Repository>()

    private var bindData = mutableListOf<Repository>()

    private lateinit var table: JBTable

    private var keyword: String? = null
    private val chosenModuleType = AtomicProperty(ChoseModuleType.Kotlin)

    init {
        init()
        title = "Auto Gradle"
        rootPane.apply {
            JBDimension(800, 450).let {
                this.preferredSize = it
                this.minimumSize = it
                this.maximumSize = it
            }
        }
    }

    private fun Cell<JBRadioButton>.applySelected(v: ChoseModuleType): Cell<JBRadioButton> {
        return onChanged {
            if (it.isSelected) {
                chosenModuleType.set(v)
            }
        }
    }

    override fun getStyle(): DialogStyle {
        return DialogStyle.COMPACT
    }

    override fun doOKAction() {
        super.doOKAction()
        insetStringAfterOffset(event)
    }

    override fun createSouthAdditionalPanel(): JPanel {
        return panel {
            buttonsGroup {
                twoColumnsRow(
                    column1 = {
                        radioButton("Kotlin", ChoseModuleType.Kotlin).applySelected(ChoseModuleType.Kotlin)
                    },
                    column2 = {
                        radioButton("Groovy", ChoseModuleType.Groovy).applySelected(ChoseModuleType.Groovy)
                    },
                )
            }.bind(chosenModuleType::get, chosenModuleType::set)
        }

    }

    override fun createCenterPanel(): JComponent {
        createTable()
        return panel {
            row {
                textField().align(AlignX.FILL).apply {
                    this.component.document.addDocumentListener(this@EntranceDialog)
                }
            }
            row {
                scrollCell(table).align(Align.FILL)
            }.resizableRow()
        }
    }

    private fun createTable() {

        listRepos()

        table = JBTable(HomeTableModel(bindData))

        table.columnModel.getColumn(0).minWidth = 50
        table.columnModel.getColumn(1).minWidth = 200
        table.columnModel.getColumn(2).minWidth = 100
        table.columnModel.getColumn(3).minWidth = 100

        table.columnModel.getColumn(0).maxWidth = 50
        table.preferredScrollableViewportSize = Dimension(800, 300)
        table.rowHeight = 36
        table.fillsViewportHeight = true
        table.addMouseListener(this)
        IconColumn(table)
    }

    override fun insertUpdate(documentEvent: DocumentEvent?) {
        search(documentEvent)
    }

    override fun removeUpdate(documentEvent: DocumentEvent?) {
        search(documentEvent)
    }

    override fun changedUpdate(documentEvent: DocumentEvent?) {
    }

    override fun mouseClicked(mouseEvent: MouseEvent?) {
        if (mouseEvent == null) return
        val row = table.rowAtPoint(mouseEvent.point)
        when (table.columnAtPoint(mouseEvent.point)) {
            4 -> startUri(bindData[row])
            else -> Unit
        }
    }

    override fun mousePressed(documentEvent: MouseEvent?) {
    }

    override fun mouseReleased(documentEvent: MouseEvent?) {
    }

    override fun mouseEntered(documentEvent: MouseEvent?) {
    }

    override fun mouseExited(documentEvent: MouseEvent?) {
    }

    private fun startUri(repo: Repository) {
        val desktop = Desktop.getDesktop()
        try {
            desktop.browse(URI(repo.url.orEmpty()))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun search(documentEvent: DocumentEvent?) {
        if (documentEvent == null) return
        val document = documentEvent.document
        try {
            keyword = document.getText(0, document.length).lowercase()
            search(keyword)
        } catch (e: BadLocationException) {
            e.printStackTrace()
        }
    }

    private fun search(input: String?) {
        bindData.clear()
        if (input.isNullOrBlank()) {
            bindData.addAll(_data)
        } else {
            bindData.addAll(
                _data.filter {
                    it.nickname.orEmpty().contains(input, ignoreCase = true)
                }
            )
        }
        table.updateUI()
    }

    private fun insetStringAfterOffset(e: AnActionEvent) {
        val editor = e.getRequiredData(CommonDataKeys.EDITOR)
        WriteCommandAction.runWriteCommandAction(e.project) {
            for (resp in bindData) {
                if (resp.isChoose == true) {
                    editor.document.insertString(editor.caretModel.offset, resp.insertResult())
                }
            }
        }
    }

    private inline fun Repository.insertResult(): String {
        return when (chosenModuleType.get()) {
            ChoseModuleType.Kotlin -> implementationToKotlin(this)
            ChoseModuleType.Groovy -> implementationToGroovy(this)
        }
    }

    private fun implementationToGroovy(repo: Repository): String {
        val usageVersion = if (repo.customVersion.isNullOrBlank()) repo.version else repo.customVersion
        return if (repo.compilerId.isNullOrBlank()) {
            """${repo.dependenceMode} '${repo.groupId}:${repo.artifactIdJava}:${usageVersion}'
    """
        } else {
            """${repo.dependenceMode} '${repo.groupId}:${repo.artifactIdJava}:${usageVersion}'
    annotationProcessor '${repo.groupId}:${repo.compilerId}:${usageVersion}'
    """
        }
    }

    private fun implementationToKotlin(repo: Repository): String {
        val usageVersion = if (repo.customVersion.isNullOrBlank()) repo.version else repo.customVersion
        return if (repo.compilerId.isNullOrBlank()) {
            """${repo.dependenceMode}("${repo.groupId}:${repo.artifactId}:${usageVersion}")
    """
        } else {
            """${repo.dependenceMode}("${repo.groupId}:${repo.artifactId}:${usageVersion}"
    kapt("${repo.groupId}:${repo.compilerId}:${usageVersion}")
    """
        }
    }

    private fun listRepos() {
        OkHttpClient().newCall(
            Request.Builder()
                .addHeader("factory-api-version", "v2.0")
                .url("https://plugins.95factory.com/api/autogradle/repository").get().build()
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                Gson().fromJson(response.body?.string(), ApiResponse::class.java)?.let { result ->
                    _data = result.data
                    bindData.clear()
                    bindData.addAll(_data)
                    table.updateUI()
                }
            }
        })
    }

}