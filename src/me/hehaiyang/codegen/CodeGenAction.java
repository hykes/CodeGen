package me.hehaiyang.codegen;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeView;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.util.DirectoryChooserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import me.hehaiyang.codegen.setting.FormatSetting;
import me.hehaiyang.codegen.utils.PsiUtil;
import me.hehaiyang.codegen.windows.MarkDownWindow;
import me.hehaiyang.codegen.windows.DatabaseWindow;

import javax.swing.*;

public class CodeGenAction extends AnAction {

    public CodeGenAction() {
        super(AllIcons.Icon_small);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = PsiUtil.getProject(anActionEvent);
        PsiPackage psiPackage = PsiUtil.choosePackage(project);
        PsiUtil.createFile(project, psiPackage,"ss.java", "public class ss{}", JavaFileType.INSTANCE);

//        FormatSetting setting = FormatSetting.getInstance();
//
//        JFrame startFrame;
//
//        if(setting.getParams().containsKey("sql") && setting.getParams().get("sql").equals("true")){
//            startFrame = new DatabaseWindow();
//        }else{
//            startFrame = new MarkDownWindow(anActionEvent);
//        }
//
//        startFrame.setSize(800, 400);
//        startFrame.setAlwaysOnTop(false);
//        startFrame.setLocationRelativeTo(null);
//        startFrame.setVisible(true);

    }

    @Override
    public void update(AnActionEvent event) {
        // 在Action显示之前，先判定是否显示此Action
        // 只有当焦点为 package 或者 class 时，显示此Action
        IdeView ideView = PsiUtil.getIdeView(event);
        Project project = PsiUtil.getProject(event);
        boolean isPackage = true;
        if(ideView != null && project != null){
            PsiDirectory psiDirectory = DirectoryChooserUtil.getOrChooseDirectory(ideView);
            if(psiDirectory != null) {
                isPackage = PsiDirectoryFactory.getInstance(project).isPackage(psiDirectory);
            }
        }
        this.getTemplatePresentation().setVisible(isPackage);
        this.getTemplatePresentation().setEnabled(isPackage);
    }

}
