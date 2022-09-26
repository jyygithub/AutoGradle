package com.jiangyy.autogradle.entity

class Reponse(
    val code: Int,
    val message: String,
    val data: MutableList<Repository>
)