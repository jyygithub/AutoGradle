package entity

class Repository(
    val name: String,
    val nickname: String,
    val user: String,
    val version: String,
    val implementation: String,
    val kapt: String?,
    var customVersion: String?,
    var warehouse: String?,
    val remark: String?,
    val description: String?,
) {
    var isChoose: Boolean = false
}