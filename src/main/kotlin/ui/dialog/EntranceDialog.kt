package ui.dialog

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.layout.panel
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.JBDimension
import entity.Repository
import entity.Tag
import okhttp3.*
import org.jetbrains.annotations.Nullable
import ui.table.HomeTableModel
import ui.table.IconColumn
import utils.Utils.inset
import utils.Utils.startUri
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.IOException
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.BadLocationException

class EntranceDialog(@Nullable private val event: AnActionEvent) : DialogWrapper(true), DocumentListener,
    MouseListener {

    private var repositoryArrayList = mutableListOf<Repository>()
    private var originalData = mutableListOf<Repository>()

    private lateinit var table: JBTable
    private lateinit var comboBox: ComboBox<Tag>

    private var mTagId = 0

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
        listRepositoryTag()
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
        comboBox = ComboBox()
        comboBox.renderer = ListCellRenderer<Tag> { p0, p1, p2, p3, p4 -> JLabel(p1?.name.orEmpty()) }
        comboBox.addActionListener {
            mTagId = (comboBox.selectedItem as Tag?)?.id ?: 0
            search(mTagId, null)
        }
        val searchPanel = JPanel().apply {
            layout = GridLayout(1, 2)
            add(comboBox)
            add(searchTextField)
        }

        createTable()
        val tablePanel = JPanel().apply {
            layout = GridLayout(1, 0)
        }
        val scrollPane = JBScrollPane(table)
        tablePanel.add(scrollPane)


        val r = panel {
            row {
                comboBox()
                searchTextField()
//                searchPanel()
            }
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
        OkHttpClient().newCall(Request.Builder().url("http://localhost:12125/v1/autogradle/repository").get().build())
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

    private fun listRepositoryTag() {
        OkHttpClient().newCall(
            Request.Builder().url("http://localhost:12125/v1/autogradle/repository/tag").get().build()
        )
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}

                override fun onResponse(call: Call, response: Response) {
                    Gson().fromJson<MutableList<Tag>>(
                        response.body!!.string(), object : TypeToken<MutableList<Tag?>?>() {}.type
                    )?.let { result ->
                        comboBox.removeAllItems()
                        result.forEach {
                            comboBox.addItem(it)
                        }
                    }

                }
            })
    }

    private fun search(tagId: Int, documentEvent: DocumentEvent) {
        val document = documentEvent.document
        try {
            val input = document.getText(0, document.length).toLowerCase()
            search(tagId, input)
        } catch (e: BadLocationException) {
            e.printStackTrace()
        }
    }

    private fun search(tagId: Int, input: String?) {
        repositoryArrayList.clear()

        when {
            tagId <= 1 && input.isNullOrBlank() -> {
                repositoryArrayList.addAll(originalData)
            }
            tagId > 1 && input.isNullOrBlank() -> {
                for (i in originalData.indices) {
                    val item = originalData[i]
                    if (tagId == item.tag_id) {
                        repositoryArrayList.add(originalData[i])
                    }
                }
            }
            tagId <= 1 && !input.isNullOrBlank() -> {
                for (i in originalData.indices) {
                    val item = originalData[i]
                    if (item.name.toLowerCase().contains(input)) {
                        repositoryArrayList.add(originalData[i])
                    }
                }
            }
            tagId > 1 && !input.isNullOrBlank() -> {
                for (i in originalData.indices) {
                    val item = originalData[i]
                    if (item.name.toLowerCase().contains(input) && tagId == item.tag_id) {
                        repositoryArrayList.add(originalData[i])
                    }
                }
            }
            else -> Unit
        }
        table.updateUI()
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
        search(mTagId, documentEvent!!)
    }

    override fun removeUpdate(documentEvent: DocumentEvent?) {
        search(mTagId, documentEvent!!)
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