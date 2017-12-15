package com.github.hykes.codegen;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.github.hykes.codegen.config.SettingManager;
import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.utils.PsiUtil;
import com.github.hykes.codegen.windows.DBWindow;
import com.github.hykes.codegen.windows.SQLWindow;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class CodeGenAction extends AnAction {

    public CodeGenAction() {
        super(AllIcons.Icon_small);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        SettingManager settingManager = SettingManager.getInstance();

        IdeaContext ideaContext = new IdeaContext();
        ideaContext.setProject(PsiUtil.getProject(anActionEvent));

//        Locale locale = new Locale("zh", "CN");
        Locale locale = new Locale("en", "US");
//        Locale locale = Locale.getDefault();

        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        JFrame startFrame;
        if(settingManager.getConfigSetting().isDbRadio()){
            startFrame = new DBWindow(ideaContext);
            startFrame.setSize(350, 180);
        }else{
            startFrame = new SQLWindow(ideaContext);
            startFrame.setSize(500, 350);
        }
//        startFrame.setTitle(bundle.getString("aaa"));

        startFrame.setResizable(false);
        startFrame.setAlwaysOnTop(true);
        startFrame.setLocationRelativeTo(null);
        startFrame.setVisible(true);
    }

}
