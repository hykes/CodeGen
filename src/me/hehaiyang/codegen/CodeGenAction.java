package me.hehaiyang.codegen;

import com.intellij.ide.IdeView;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.util.DirectoryChooserUtil;
import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.util.PsiTreeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CodeGenAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

        Project  project = anActionEvent.getData(PlatformDataKeys.PROJECT);
//        PsiClass defaultClass = getPsiClassFromContext(anActionEvent);
//        chooseClass(project, defaultClass);

//        CodeGenWindow codeGenWindow=new CodeGenWindow(anActionEvent);
//
//        codeGenWindow.setSize(800, 400);
//        codeGenWindow.setAlwaysOnTop(false);
//        codeGenWindow.setLocationRelativeTo(null);
//        codeGenWindow.setVisible(true);
        InputStream in = this.getClass().getResourceAsStream("/template/DaoTemplate.hbs");
        try {
            System.out.print(inputStream2String(in));
        }catch (Exception e){
            
        }

//        createFile(anActionEvent, project, "ss"+ JavaFileType.DOT_DEFAULT_EXTENSION, JavaFileType.INSTANCE, "ss");
    }

    public static String inputStream2String(InputStream is)  throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while((i=is.read())!=-1){
            baos.write(i);
        }
        return baos.toString();
    }

    @Override
    public void update(AnActionEvent event) {
        //在Action显示之前,先判定是否显示此Action
        IdeView ideView = event.getData(LangDataKeys.IDE_VIEW);
        Project  project = event.getData(PlatformDataKeys.PROJECT);
        PsiDirectory psiDirectory = DirectoryChooserUtil.getOrChooseDirectory(ideView);
        boolean isPackage = false;
        if(psiDirectory != null) {
             isPackage = PsiDirectoryFactory.getInstance(project).isPackage(psiDirectory);
        }
        this.getTemplatePresentation().setVisible(isPackage);
        this.getTemplatePresentation().setEnabled(isPackage);
    }

    /**
     * 类选择器
     * @param project
     * @param defaultClass
     * @return
     */
    public static PsiClass chooseClass(Project project, PsiClass defaultClass) {
        TreeClassChooser chooser = TreeClassChooserFactory.getInstance(project)
                .createProjectScopeChooser("Select a class", defaultClass);

        chooser.showDialog();

        return chooser.getSelected();
    }

    /**
     * 获取当前选择的类文件
     * @param e
     * @return
     */
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

    /**
     * 创建文件
     * @param anActionEvent
     * @param project
     * @param fileName
     * @param fileType
     * @param context
     */
    private void createFile(AnActionEvent anActionEvent, Project project, String fileName, LanguageFileType fileType, String context) {
        PsiFile psiFile=  PsiFileFactory.getInstance(project).createFileFromText(fileName , fileType, context);
        IdeView ideView = anActionEvent.getData(LangDataKeys.IDE_VIEW);
        PsiDirectory psiDirectory = DirectoryChooserUtil.getOrChooseDirectory(ideView);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            psiDirectory.add(psiFile);
        });
    }

}
