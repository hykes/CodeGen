package com.github.hykes.codegen;

import com.github.hykes.codegen.gui.SqlEditorPanel;
import com.github.hykes.codegen.messages.CodeGenBundle;
import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.utils.NotifyUtil;
import com.github.hykes.codegen.utils.Icons;
import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;

import javax.swing.*;

/**
 * 解析sql生成代码
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/03/21
 */
public class SQLGeneratorAction extends AnAction implements DumbAware {

    public SQLGeneratorAction() {
        super(Icons.CODEGEN);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        Project project = PsiUtil.getProject(anActionEvent);
        DumbService dumbService = DumbService.getInstance(project);
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification(CodeGenBundle.message("codegen.plugin.is.not.available.during.indexing"));
            return;
        }

        JFrame frame = new JFrame();
        frame.setTitle("CodeGen-SQL");
        frame.setContentPane(new SqlEditorPanel(new IdeaContext(project)).getRootComponent());
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
