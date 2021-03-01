package utils

import entity.Repository
import java.awt.Desktop
import java.lang.Exception
import java.net.URI

object Utils {

    @JvmStatic
    fun inset(repo: Repository): String {
        val usageVersion = if (repo.customVersion.isNullOrEmpty()) repo.version else repo.customVersion
        if (repo.kapt.isNullOrEmpty()) {
            return """implementation '${repo.implementation}${usageVersion}'
    """
        }
        return """implementation '${repo.implementation}${usageVersion}'
    kapt '${repo.kapt}${repo.version}'
    """
    }

    @JvmStatic
    fun startUri(repo: Repository) {
        val desktop = Desktop.getDesktop()
        try {
            val uri = when (repo.warehouse) {
                "github" -> URI("http://www.github.com/${repo.user}/${repo.name}")
                "android" -> URI("https://developer.android.google.cn/jetpack/androidx/releases/${repo.name}")
                else -> URI("http://www.github.com/${repo.user}/${repo.name}")
            }
            desktop.browse(uri)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}