package com.jiangyy.autogradle.ui

import com.google.gson.Gson
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.layout.panel
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.JBDimension
import com.jiangyy.autogradle.entity.Reponse
import com.jiangyy.autogradle.entity.Repository
import com.jiangyy.autogradle.utils.orDefault
import okhttp3.*
import org.jetbrains.annotations.Nullable
import ui.table.IconColumn
import java.awt.Desktop
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.IOException
import java.net.URI
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.BadLocationException

class EntranceDialog(@Nullable private val event: AnActionEvent) : DialogWrapper(true), DocumentListener,
    MouseListener, ItemListener {

    private var bindData = mutableListOf<Repository>()
    private var originalData = mutableListOf<Repository>()

    private lateinit var table: JBTable
    private lateinit var comboBox: ComboBox<String>

    private var key: String? = null
    private var keyword: String? = null
    private var isJava = false

    init {
        init()
        title = "Auto Gradle"
        rootPane.apply {
            JBDimension(800, 450).let {
                this.preferredSize = it
                this.minimumSize = it
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

        val javaButton = JBRadioButton("Java")
        val kotlinButton = JBRadioButton("Kotlin", true)

        javaButton.addActionListener {
            isJava = true
            key = null
            keyword = null
            javaButton.isSelected = true
            kotlinButton.isSelected = false
            comboBox.selectedIndex = 0
            (table.model as HomeTableModel).updateLanguage(isJava)
        }
        kotlinButton.addActionListener {
            isJava = false
            key = null
            keyword = null
            javaButton.isSelected = false
            kotlinButton.isSelected = true
            comboBox.selectedIndex = 0
            (table.model as HomeTableModel).updateLanguage(isJava)
        }
        comboBox = ComboBox<String>().apply {
            addItem("All")
            addItem("Androidx")
            addItem("Cache")
            addItem("Chart")
            addItem("CustomView")
            addItem("Debug")
            addItem("Dialog")
            addItem("Http")
            addItem("Image")
            addItem("Json")
            addItem("Kit")
            addItem("Log")
            addItem("Permission")
            addItem("Picker")
            addItem("RecyclerView")
            addItem("Subscribe")
            addItem("WebView")
        }
        comboBox.addItemListener(this)
        return panel {
            row {
                comboBox()
                kotlinButton()
                javaButton()
            }
        }
    }

    override fun createCenterPanel(): JComponent {
        val searchTextField = JTextField()
        searchTextField.document.addDocumentListener(this)
        createTable()
        val tablePanel = JPanel(GridLayout(1, 0))
        val scrollPane = JBScrollPane(table)
        tablePanel.add(scrollPane)

        val r = panel {
            row {
                searchTextField()
            }
            row {
                scrollPane()
            }
        }

        return r
    }

    override fun itemStateChanged(e: ItemEvent?) {
        if (e == null) return
        if (e.stateChange == ItemEvent.SELECTED) {
            key = if (comboBox.selectedIndex == 0) {
                null
            } else {
                comboBox.getItemAt(comboBox.selectedIndex)
            }
        }
        search(keyword, key)
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
        val row = table.rowAtPoint(mouseEvent!!.point)
        val col = table.columnAtPoint(mouseEvent.point)
        when (col) {
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
            search(keyword, key)
        } catch (e: BadLocationException) {
            e.printStackTrace()
        }
    }

    private fun search(input: String?, key: String?) {
        bindData.clear()
        when {
            input.isNullOrBlank() && key.isNullOrBlank() -> bindData.addAll(originalData)
            key.isNullOrBlank() -> {
                for (i in originalData.indices) {
                    val item = originalData[i]
                    if (
                        item.nickname.orEmpty().lowercase().contains(input.orEmpty())
                    ) {
                        bindData.add(originalData[i])
                    }
                }
            }

            else -> {
                for (i in originalData.indices) {
                    val item = originalData[i]
                    if (
                        item.nickname.orEmpty().lowercase().contains(input.orEmpty())
                        &&
                        item.key.orEmpty().lowercase() == key.orEmpty().lowercase()
                    ) {
                        bindData.add(originalData[i])
                    }
                }
            }
        }
        table.updateUI()
    }

    private fun insetStringAfterOffset(e: AnActionEvent) {
        val editor = e.getRequiredData(CommonDataKeys.EDITOR)
        WriteCommandAction.runWriteCommandAction(e.project) {
            for (resp in bindData) {
                if (resp.isChoose.orDefault()) {
                    editor.document.insertString(editor.caretModel.offset, inset(resp))
                }
            }
        }
    }

    private fun inset(repo: Repository): String {
        return if (isJava) insertJava(repo)
        else insertKotlin(repo)
    }


    private fun insertJava(repo: Repository): String {
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

    private fun insertKotlin(repo: Repository): String {
        val usageVersion = if (repo.customVersion.isNullOrBlank()) repo.version else repo.customVersion
        return if (repo.compilerId.isNullOrBlank()) {
            """${repo.dependenceMode} '${repo.groupId}:${repo.artifactId}:${usageVersion}'
    """
        } else {
            """${repo.dependenceMode} '${repo.groupId}:${repo.artifactId}:${usageVersion}'
    kapt '${repo.groupId}:${repo.compilerId}:${usageVersion}'
    """
        }
    }

    private fun listRepos() {
        OkHttpClient().newCall(
            Request.Builder()
                .addHeader("factory-api-version", "v2.0")
                .url("https://95factory.com/autogradle/repository").get().build()
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                Gson().fromJson<Reponse>(response.body?.string(), Reponse::class.java)?.let { result ->
                    originalData = result.data
                    bindData.clear()
                    bindData.addAll(result.data)
                    table.updateUI()
                }

            }
        })
    }

}