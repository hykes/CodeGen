package com.github.hykes.codegen;

import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.gui.SqlEditorPanel;
import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 解析sql生成代码
 *
 * @author: hehaiyangwork@qq.com
 * @date: 2017/03/21
 */
public class SQLGeneratorAction extends AnAction {

    public SQLGeneratorAction() {
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

        JFrame frame = new JFrame();
        frame.setContentPane(new SqlEditorPanel(ideaContext).$$$getRootComponent$$$());
        frame.setSize(500, 350);

//        startFrame.setTitle(bundle.getString("aaa"));

        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
