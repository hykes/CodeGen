package me.hehaiyang.codegen;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import me.hehaiyang.codegen.setting.SettingManager;
import me.hehaiyang.codegen.windows.DatabaseWindow;
import me.hehaiyang.codegen.windows.MarkDownWindow;

import javax.swing.*;

public class CodeGenAction extends AnAction {

    public CodeGenAction() {
        super(AllIcons.Icon_small);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
//        Project project = PsiUtil.getProject(anActionEvent);
//        PsiDirectory psiDirectory = PsiUtil.browseForFile(project);
//        PsiUtil.createFile(project, psiDirectory,"VariablesUI.java", "public class ss{}", JavaFileType.INSTANCE);

        SettingManager settingManager = SettingManager.getInstance();

        JFrame startFrame;

        if(settingManager.getConfigSetting().isDatabaseBox()){
            startFrame = new DatabaseWindow();
        }else{
            startFrame = new MarkDownWindow(anActionEvent);
        }

        startFrame.setSize(800, 400);
        startFrame.setAlwaysOnTop(true);
        startFrame.setLocationRelativeTo(null);
        startFrame.setVisible(true);
    }

}
