package ui.dialog

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.layout.panel
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.JBDimension
import entity.Repository
import okhttp3.*
import org.jetbrains.annotations.Nullable
import ui.home.HomeTableModel
import ui.table.IconColumn
import utils.Utils.inset
import utils.Utils.startUri
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.IOException
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.BadLocationException

class EntranceDialog(@Nullable private val event: AnActionEvent) : DialogWrapper(true), DocumentListener,
    MouseListener {

    private var repositoryArrayList = mutableListOf<Repository>()
    private var originalData = mutableListOf<Repository>()
    private lateinit var table: JBTable

    init {
        init()
        title = "Auto Gradle"
        rootPane.apply {
            JBDimension(800, 450).let {
                this.preferredSize = it
                this.minimumSize = it
            }
        }
        listRepository()
    }

    override fun getStyle(): DialogStyle {
        return DialogStyle.COMPACT
    }

    override fun doOKAction() {
        super.doOKAction()
        insetStringAfterOffset(event)
    }

    override fun createCenterPanel(): JComponent {
        val searchTextField = JTextField()
        searchTextField.document.addDocumentListener(this)
        createTable()
        val tablePanel = JPanel().apply {
            layout = GridLayout(1, 0)
        }
        val scrollPane = JBScrollPane(table)
        tablePanel.add(scrollPane)

        val r = panel {
            row { searchTextField() }
            row { scrollPane() }
        }

        return r
    }

    private fun createTable() {
        table = JBTable(HomeTableModel(repositoryArrayList))

        table.columnModel.getColumn(0).minWidth = 50
        table.columnModel.getColumn(1).minWidth = 200
        table.columnModel.getColumn(2).minWidth = 100
        table.columnModel.getColumn(3).minWidth = 100

        table.columnModel.getColumn(0).maxWidth = 50
        table.preferredScrollableViewportSize = Dimension(800, 300)
        table.rowHeight = 36
        table.fillsViewportHeight = true
        table.addMouseListener(this)
        IconColumn(table, 4)
    }

    private fun listRepository() {
        OkHttpClient().newCall(Request.Builder().url("https://95factory.com/v1/autogradle/repository").get().build())
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}

                override fun onResponse(call: Call, response: Response) {

                    originalData = Gson().fromJson(
                        response.body!!.string(), object : TypeToken<MutableList<Repository?>?>() {}.type
                    )

                    repositoryArrayList.clear()
                    repositoryArrayList.addAll(originalData)
                    table.updateUI()
                }
            })
    }

    private fun search(documentEvent: DocumentEvent) {
        val document = documentEvent.document
        try {
            val input = document.getText(0, document.length).toLowerCase()
            repositoryArrayList.clear()
            if (input.isEmpty()) {
                repositoryArrayList.addAll(originalData)
            } else {
                for (i in originalData.indices) {
                    if (originalData[i].name.toLowerCase().contains(input)) {
                        repositoryArrayList.add(originalData[i])
                    }
                }
            }
            table.updateUI()
        } catch (e: BadLocationException) {
            e.printStackTrace()
        }
    }

    private fun insetStringAfterOffset(e: AnActionEvent) {
        val editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val caretModel = editor.caretModel
        val offset = caretModel.offset
        WriteCommandAction.runWriteCommandAction(e.project) {
            for (resp in repositoryArrayList) {
                if (resp.isChoose) {
                    editor.document.insertString(offset, inset(resp))
                }
            }
        }
    }

    override fun insertUpdate(documentEvent: DocumentEvent?) {
        search(documentEvent!!)
    }

    override fun removeUpdate(documentEvent: DocumentEvent?) {
        search(documentEvent!!)
    }

    override fun changedUpdate(documentEvent: DocumentEvent?) {
    }

    override fun mouseClicked(mouseEvent: MouseEvent?) {
        val row = table.rowAtPoint(mouseEvent!!.point)
        val col = table.columnAtPoint(mouseEvent.point)
        when (col) {
            4 -> startUri(repositoryArrayList[row])
            else -> {
            }
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
}