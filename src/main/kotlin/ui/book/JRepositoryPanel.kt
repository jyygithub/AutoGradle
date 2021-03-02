package ui.book

import entity.Repository
import java.awt.GridLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class JRepositoryPanel : JPanel() {

    private val labelName: JLabel = JLabel().apply {
//        border = EmptyBorder(10, 10, 0, 10)
    }
    private val labelImplementation: JLabel = JLabel().apply {
//        border = EmptyBorder(0, 10, 0, 10)
    }

    init {
        border = EmptyBorder(10, 10, 10, 10)
        layout = GridLayout(1, 1)
        add(labelName)
    }

    fun setRepository(repository: Repository) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("<html><body>")
        stringBuilder.append("""<b style="font-size: 14px">${repository.user}/${repository.name}</b></h2><br/>""")
        stringBuilder.append("<i>${repository.description.orEmpty()}</i><br/>")
        stringBuilder.append("""implement '${repository.implementation}${repository.version}'""")
        if(!repository.kapt.isNullOrEmpty()){
            stringBuilder.append("""<br/>kapt '${repository.kapt}${repository.version}'""")
        }
        stringBuilder.append("</body></html>")
        labelName.text = stringBuilder.toString()
    }
}