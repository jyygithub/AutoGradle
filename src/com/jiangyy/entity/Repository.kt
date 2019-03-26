package com.jiangyy.entity

class Repository(

        var isChoose: Boolean,
        var name: String,
        var user: String,
        var version: String,
        var gradle: String,
        var processor: String,
        var maven: String,
        var remark: String,
        var latest_version: String
)

class Resp(
        val created_at: String,
        val name: String,
        val published_at: String,
        val tag_name: String
)