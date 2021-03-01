package ui.book

import entity.Repository
import java.awt.GridLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class JRepositoryPanel : JPanel() {

    private val labelImplementation: JLabel = JLabel()

    init {
        border = EmptyBorder(10, 10, 10, 10)
        layout = GridLayout(1, 1)
        add(labelImplementation)
    }

    fun setRepository(repository: Repository) {
        val result = StringBuilder()
        result.append("<html>")
        result.append("<font size=5><b>${repository.user}/${repository.name}</b></font>")
        result.append("<br>")
        result.append("<em>${repository.description}</em>")
        result.append("<br>")
        result.append("implementation \'${repository.implementation}${repository.version}\'")
        if (!repository.kapt.isNullOrEmpty()) {
            result.append("<br>")
            result.append("kapt \'${repository.kapt.orEmpty()}${repository.version}\'")
        }
        result.append("</html>")
        labelImplementation.text = result.toString()
    }

}