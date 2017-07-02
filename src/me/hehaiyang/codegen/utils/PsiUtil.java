package me.hehaiyang.codegen.utils;

import com.intellij.ide.IdeView;
import com.intellij.ide.util.*;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.PackageChooser;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/6
 */
public class PsiUtil {

    public static Project getProject(AnActionEvent anActionEvent) {
        return anActionEvent.getData(PlatformDataKeys.PROJECT);
    }

    public static IdeView getIdeView(AnActionEvent anActionEvent) {
        return anActionEvent.getData(LangDataKeys.IDE_VIEW);
    }

    /**
     * 获取当前焦点下的类
     * @param anActionEvent
     * @return
     */
    public static PsiClass getPsiClass(AnActionEvent anActionEvent) {

        PsiFile psiFile = anActionEvent.getData(LangDataKeys.PSI_FILE);
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);

        if (psiFile == null || editor == null) {
            return null;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);

        return PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }

    /**
     * 类选择器
     * @param anActionEvent
     * @return
     */
    public static PsiClass chooseClass(AnActionEvent anActionEvent) {
        Project project = getProject(anActionEvent);
        PsiClass defaultClass = getPsiClass(anActionEvent);
        return chooseClass(project, defaultClass);
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

    public static PsiPackage choosePackage(Project project) {
        PackageChooser chooser = new PackageChooserDialog("Select a package", project);
        chooser.show();
        return chooser.getSelectedPackage();
    }

    public static PsiDirectory chooseDirectory(Project project) {
        DirectoryChooserView view = new DirectoryChooserModuleTreeView(project);
        DirectoryChooser chooser = new DirectoryChooser(project, view);
        chooser.showAndGet();

        return chooser.getSelectedDirectory();
    }

    public static PsiDirectory createDirectory(Project project, String title, String description) {
        final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        descriptor.setTitle(title);
        descriptor.setShowFileSystemRoots(false);
        descriptor.setDescription(description);
        descriptor.setHideIgnored(true);
        descriptor.setRoots(project.getBaseDir());
        descriptor.setForcedToUseIdeaFileChooser(true);
        VirtualFile file = FileChooser.chooseFile(descriptor, project, project.getBaseDir());
        if(Objects.isNull(file)){
            Messages.showInfoMessage("取消当前模版组生成。", "Error");
            return null;
        }

        PsiDirectory psiDirectory = PsiDirectoryFactory.getInstance(project).createDirectory(file);
        if(PsiDirectoryFactory.getInstance(project).isPackage(psiDirectory)){
            return psiDirectory;
        }else {
            Messages.showInfoMessage("请选择正确的 package 路径。", "Error");
            return createDirectory(project, title, description);
        }
    }

    public static void createFile(Project project, @NotNull PsiPackage psiPackage, String fileName, String context, LanguageFileType fileType) {
        createFile(project, psiPackage.getDirectories()[0], fileName, context, fileType);
    }

    public static void createFile(Project project, IdeView ideView, String fileName, String context, LanguageFileType fileType) {
        PsiFile psiFile=  PsiFileFactory.getInstance(project).createFileFromText(fileName, fileType, context);
        PsiDirectory psiDirectory = DirectoryChooserUtil.getOrChooseDirectory(ideView);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            psiDirectory.add(psiFile);
        });
    }

    public static void createFile(Project project, @NotNull PsiDirectory psiDirectory, String fileName, String context, LanguageFileType fileType) {
        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText(fileName, fileType, context);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            psiDirectory.add(psiFile);
        });
    }

}
