package me.hehaiyang.codegen.windows;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;

/**
 * toolWindow
 */
public class CodeGenToolsWindow implements ToolWindowFactory {

    private JPanel toolsWindowPanel;

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(toolsWindowPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

}
