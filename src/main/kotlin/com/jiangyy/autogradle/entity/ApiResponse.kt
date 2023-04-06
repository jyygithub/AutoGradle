package com.jiangyy.autogradle.entity

class ApiResponse(
    val code: Int,
    val message: String,
    val data: MutableList<Repository>
)