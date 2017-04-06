package me.hehaiyang.codegen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import me.hehaiyang.codegen.utils.PsiUtil;

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

        Project project = PsiUtil.getProject(event);
        PsiClass clazz = PsiUtil.getPsiClass(event);
        PsiField[] fields =  clazz.getFields();

        StringBuilder ss = new StringBuilder("");
        ss.append("@@@");
        for(PsiField f: fields){
            ss.append(f.getType().getClass().getGenericSuperclass().getTypeName() + "-" + f.getName() + "\n");
        }
        ss.append("@@@");

        Messages.showMessageDialog(project, ss.toString(), "Information", Messages.getInformationIcon());
    }

}
