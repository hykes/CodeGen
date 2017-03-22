package me.hehaiyang.codegen;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import me.hehaiyang.codegen.windows.CodeGenWindow;

public class CodeGenAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

//        Project  project = anActionEvent.getData(PlatformDataKeys.PROJECT);
//        PsiClass defaultClass = getPsiClassFromContext(anActionEvent);
//        chooseClass(project, defaultClass);
        CodeGenWindow codeGenWindow=new CodeGenWindow(anActionEvent);

        codeGenWindow.setSize(800, 400);
        codeGenWindow.setAlwaysOnTop(false);
        codeGenWindow.setLocationRelativeTo(null);
        codeGenWindow.setVisible(true);
    }

    @Override
    public void update(AnActionEvent event) {
        //在Action显示之前,根据选中文件扩展名判定是否显示此Action
        VirtualFile file = DataKeys.VIRTUAL_FILE.getData(event.getDataContext());
        String extension = file == null ? null : file.getExtension();
        this.getTemplatePresentation().setEnabled(extension != null && "java".equals(extension));
    }

    public static PsiClass chooseClass(Project project, PsiClass defaultClass) {
        TreeClassChooser chooser = TreeClassChooserFactory.getInstance(project)
                .createProjectScopeChooser("Select a class", defaultClass);

        chooser.showDialog();

        return chooser.getSelected();
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
