package icons

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object Icons {

    @JvmField
    val PluginIcon: Icon = IconLoader.getIcon("/META-INF/pluginIcon.svg")

    @JvmField
    val GithubIcon: Icon = IconLoader.getIcon("/META-INF/github.svg")

    @JvmField
    val AndroidIcon: Icon = IconLoader.getIcon("/META-INF/android.svg")

}