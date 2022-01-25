package entity

class TagRepo(
    val code: Int,
    val message: String,
    val data: MutableList<Tag>
)

class RepoRepo(
    val code: Int,
    val message: String,
    val data: MutableList<Repository>
)