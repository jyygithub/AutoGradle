package utils

import entity.Repository
import java.awt.Desktop
import java.lang.Exception
import java.net.URI

object Utils {

    @JvmStatic
    fun inset(repo: Repository): String {
//        val usageVersion = if (repo.customVersion.isNullOrEmpty()) repo.version else repo.customVersion
//        if (repo.kapt.isNullOrEmpty()) {
//            return """implementation '${repo.implementation}${usageVersion}'
//    """
//        }
//        return """implementation '${repo.implementation}${usageVersion}'
//    kapt '${repo.kapt}${repo.version}'
//    """
        return ""
    }

    @JvmStatic
    fun startUri(repo: Repository) {
        val desktop = Desktop.getDesktop()
        try {
            desktop.browse(URI(repo.url))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}