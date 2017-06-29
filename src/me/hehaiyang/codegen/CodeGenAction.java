package me.hehaiyang.codegen;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import me.hehaiyang.codegen.model.IdeaContext;
import me.hehaiyang.codegen.config.SettingManager;
import me.hehaiyang.codegen.utils.PsiUtil;
import me.hehaiyang.codegen.windows.DatabaseWindow;
import me.hehaiyang.codegen.windows.TextWindow;

import javax.swing.*;

public class CodeGenAction extends AnAction {

    public CodeGenAction() {
        super(AllIcons.Icon_small);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        SettingManager settingManager = SettingManager.getInstance();

        IdeaContext ideaContext = new IdeaContext();
        ideaContext.setProject(PsiUtil.getProject(anActionEvent));

        JFrame startFrame;
        if(settingManager.getConfigSetting().isDatabaseRadio()){
            startFrame = new DatabaseWindow(ideaContext);
            startFrame.setSize(350, 180);
        }else{
            startFrame = new TextWindow(ideaContext);
            startFrame.setSize(500, 350);
        }

        startFrame.setResizable(false);
        startFrame.setAlwaysOnTop(true);
        startFrame.setLocationRelativeTo(null);
        startFrame.setVisible(true);
    }

}
