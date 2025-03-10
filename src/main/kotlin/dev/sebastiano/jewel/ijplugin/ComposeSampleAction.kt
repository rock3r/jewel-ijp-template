package dev.sebastiano.jewel.ijplugin

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.JBUI
import org.jetbrains.jewel.bridge.JewelComposePanel
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.enableNewSwingCompositing
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Text
import java.awt.Component
import java.awt.Dimension
import javax.swing.Action
import javax.swing.JComponent

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
                DefaultButton(onClick = { count++ }) {
                    Text(MyBundle.message("increment"))
                }
            }
        }
        component.preferredSize = preferredSize
        component.minimumSize = minimumSize

        return component
    }
}

private val DEFAULT_PREFERRED_SIZE: Dimension = JBUI.size(600, 350)
private val DEFAULT_MIN_SIZE: Dimension = JBUI.size(400, 250)
