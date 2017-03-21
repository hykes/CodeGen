package me.hehaiyang.codegen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * Created by hehaiyang on 2017/3/10.
 */
public class TextBoxes extends AnAction {

    // If you register the action from Java code, this constructor is used to set the menu item name
    // (optionally, you can specify the menu description and an icon to display next to the menu item).
    // You can omit this constructor when registering the action in the plugin.xml file.
    public TextBoxes() {
        // Set the menu item name.
        super("Text _Boxes");
        // Set the menu item name, description and icon.
        // super("Text _Boxes","Item description",IconLoader.getIcon("/Mypackage/icon.png"));
    }

    public void actionPerformed(AnActionEvent event) {


//        createjavafile(event);

        PsiClass clazz =this.getPsiClassFromContext(event);
        PsiField[] fields =  clazz.getFields();
        StringBuilder ss = new StringBuilder("");
        ss.append("@@@");
        for(PsiField f: fields){
            ss.append(f.getType().getClass().getGenericSuperclass().getTypeName() + "-" + f.getName() + "\n");
        }
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Messages.showMessageDialog(project, ss.toString(), "Information", Messages.getInformationIcon());


//        Project project = event.getData(PlatformDataKeys.PROJECT);
//        String txt= Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon());
//        Messages.showMessageDialog(project, "Hello, " + txt + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
    }

    private void createjavafile(AnActionEvent anActionEvent) {
        PsiFile psiFilecurrent = anActionEvent.getData(LangDataKeys.PSI_FILE);
        JavaDirectoryService.getInstance().createClass(psiFilecurrent.getContainingDirectory(), "MYjava");
    }

    private PsiClass getPsiClassFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);
        return PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }
}
