package action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ui.dialog.EntranceDialog

class AutoGradleAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        EntranceDialog(e).show()
    }

}