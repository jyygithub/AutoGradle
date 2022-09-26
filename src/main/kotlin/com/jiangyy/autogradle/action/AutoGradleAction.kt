package com.jiangyy.autogradle.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jiangyy.autogradle.ui.EntranceDialog

class AutoGradleAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        EntranceDialog(e).show()
    }

}