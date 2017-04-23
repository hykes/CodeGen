package me.hehaiyang.codegen;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeView;
import com.intellij.ide.util.DirectoryChooserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import me.hehaiyang.codegen.setting.FormatSetting;
import me.hehaiyang.codegen.utils.PsiUtil;
import me.hehaiyang.codegen.windows.CodeGenWindow;
import me.hehaiyang.codegen.windows.DatabaseWindow;

public class CodeGenAction extends AnAction {

    public CodeGenAction() {
        super(AllIcons.Icon_small);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        FormatSetting setting = FormatSetting.getInstance();


        Integer type = 1;

        for(String va : setting.getParams().values()){
            if("sql".equals(va)){
                type = 2;
                break;
            }
        }
        if(type.equals(1)){
            CodeGenWindow codeGenWindow=new CodeGenWindow(anActionEvent);

            codeGenWindow.setSize(800, 400);
            codeGenWindow.setAlwaysOnTop(false);
            codeGenWindow.setLocationRelativeTo(null);
            codeGenWindow.setVisible(true);
        }else{
            DatabaseWindow databaseWindow=new DatabaseWindow();

            databaseWindow.setSize(800, 400);
            databaseWindow.setAlwaysOnTop(false);
            databaseWindow.setLocationRelativeTo(null);
            databaseWindow.setVisible(true);
        }




    }

    @Override
    public void update(AnActionEvent event) {
        // 在Action显示之前，先判定是否显示此Action
        // 只有当焦点为 package 或者 class 时，显示此Action
        IdeView ideView = PsiUtil.getIdeView(event);
        Project project = PsiUtil.getProject(event);
        boolean isPackage = false;
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
