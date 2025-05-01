package dev.sebastiano.jewel.ijplugin

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.*
import com.intellij.openapi.ui.*
import com.intellij.util.ui.*
import org.jetbrains.jewel.bridge.*
import org.jetbrains.jewel.foundation.*
import org.jetbrains.jewel.ui.component.*
import java.awt.*
import javax.swing.*

class ComposeSampleAction : DumbAwareAction() {
  override fun actionPerformed(event: AnActionEvent) {
    MyDialogWrapper(event.project).show()
  }
}

internal class MyDialogWrapper(
  project: Project?,
  parent: Component? = null,
  private val minimumSize: Dimension = DEFAULT_MIN_SIZE,
  private val preferredSize: Dimension = DEFAULT_PREFERRED_SIZE,
) : DialogWrapper(project, parent, true, IdeModalityType.IDE) {

  init {
    title = "Jewel Inside"
    init()
  }

  override fun createActions(): Array<Action> = arrayOf()

  // Don't include the default border; our banners need to span the entire width
  override fun createContentPaneBorder() = null

  // Don't include the bottom panel; we'll make buttons ourselves
  override fun createSouthPanel(): JComponent? = null

  @OptIn(ExperimentalJewelApi::class)
  override fun createCenterPanel(): JComponent {
    enableNewSwingCompositing()

    val component = JewelComposePanel {
      Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        var count by remember { mutableIntStateOf(0) }
        Text(MyBundle.message("helloWorld", count))
        Spacer(Modifier.width(8.dp))
        DefaultButton(onClick = { count++ }) { Text(MyBundle.message("increment")) }
      }
    }
    component.preferredSize = preferredSize
    component.minimumSize = minimumSize

    return component
  }
}

private val DEFAULT_PREFERRED_SIZE: Dimension = JBUI.size(600, 350)
private val DEFAULT_MIN_SIZE: Dimension = JBUI.size(400, 250)
