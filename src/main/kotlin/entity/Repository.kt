package entity

data class Repository(
    val name: String,
    val nickname: String,
    var icon: String?,
    var tagId: Int,
    var tagName: String,
    val url: String,
    val version: String?,
) {
    var isChoose: Boolean = false
    var customVersion: String? = null
}