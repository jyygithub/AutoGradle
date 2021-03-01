package action

import HomeDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class AutoGradleAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        HomeDialog(e).show()
    }

}