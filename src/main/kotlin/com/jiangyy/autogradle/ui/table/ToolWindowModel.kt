package com.jiangyy.autogradle.ui.table

import com.jiangyy.autogradle.entity.Repository
import javax.swing.AbstractListModel

class ToolWindowModel(private val repositories: MutableList<Repository>) : AbstractListModel<Repository>() {

    override fun getSize(): Int {
       return repositories.size
    }

    override fun getElementAt(index: Int): Repository {
        return repositories[index]
    }
}