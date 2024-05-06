package com.jiangyy.autogradle.entity

class Repository(
    var isChoose: Boolean?,
    var name: String?,
    var key: String?,
    var nickname: String?,
    var groupId: String?,
    var artifactId: String?,
    var artifactIdJava: String?,
    var compilerId: String?,
    var dependenceMode: String?,
    var version: String?,
    var customVersion: String?,
    var mavenType: Int?,// 0 - mavenCentral; 1 - jitpack; 2 - jCenter; 3 - androidx
    var urlType: Int?, // 0 - github; 1 - android; 2 - gitee; 3 - gitlab; 4 - other;
    var url: String?,
)