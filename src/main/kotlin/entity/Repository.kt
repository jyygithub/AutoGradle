package entity

data class Repository(
    val description: String,
    val implementation: String,
    val kapt: String,
    val name: String,
    val nickname: String,
    val remark: String,
    val tag: String?,
    var tagId: Int = 0,
    val user: String,
    val version: String,
    var warehouse: String?
) {
    var isChoose: Boolean = false
    var customVersion: String? = null
}