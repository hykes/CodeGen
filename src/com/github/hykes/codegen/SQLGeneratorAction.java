package com.github.hykes.codegen;

import com.github.hykes.codegen.gui.SqlEditorPanel;
import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;

import javax.swing.*;

/**
 * 解析sql生成代码
 *
 * @author: hehaiyangwork@qq.com
 * @date: 2017/03/21
 */
public class SQLGeneratorAction extends AnAction implements DumbAware {

    public SQLGeneratorAction() {
        super(AllIcons.Icon_small);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        Project project = PsiUtil.getProject(anActionEvent);
        DumbService dumbService = DumbService.getInstance(project);
        if (dumbService.isDumb()) {
            dumbService.showDumbModeNotification("CodeGen plugin is not available during indexing !");
            return;
        }

        JFrame frame = new JFrame();
        frame.setContentPane(new SqlEditorPanel(new IdeaContext(project)).$$$getRootComponent$$$());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
