package me.hehaiyang.codegen.utils;

import com.intellij.ide.IdeView;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.impl.ProjectUtil;
import com.intellij.ide.util.*;
import com.intellij.internal.psiView.PsiViewerDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserDialog;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.PackageChooser;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.TabbedPaneWrapper;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

import java.util.List;

/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/4/6
 */
public class PsiUtil {

    /**
     * 新建文件
     * @param project
     * @param ideView
     * @param fileName
     * @param context
     * @param fileType
     */
    public static void createFile(Project project, IdeView ideView, String fileName, String context, LanguageFileType fileType) {
        PsiFile psiFile=  PsiFileFactory.getInstance(project).createFileFromText(fileName, fileType, context);
        PsiDirectory psiDirectory = DirectoryChooserUtil.getOrChooseDirectory(ideView);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            psiDirectory.add(psiFile);
        });
    }


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


//    public static PsiPackage chooseFile(Project project) {
//        FileChooser chooser = TreeFileChooserFactory.getInstance(project).createFileChooser()
//        chooser.show();
//        return chooser.getSelectedPackage();
//    }

    public static PsiDirectory browseForFile(Project project) {
        final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        descriptor.setTitle("Select parent folder");
        descriptor.setShowFileSystemRoots(false);
        descriptor.setDescription("description");
        descriptor.setHideIgnored(true);
        descriptor.setRoots(project.getBaseDir());
        VirtualFile file = FileChooser.chooseFile(descriptor, project, project.getBaseDir());
        PsiDirectory psiDirectory = PsiDirectoryFactory.getInstance(project).createDirectory(file);
        if(PsiDirectoryFactory.getInstance(project).isPackage(psiDirectory)){
            return psiDirectory;
        }else {
            Messages.showInfoMessage("请选择正确的 package 路径。", "Action Error !");
            return browseForFile(project);
        }
    }

    public static PsiDirectory chooseDirectory(Project project) {
        DirectoryChooserView view = new DirectoryChooserModuleTreeView(project);
        DirectoryChooser chooser = createDirectoryChooserWithoutNeighborClasses(project);
        chooser.showAndGet();

//        DirectoryChooser directoryChooser = createDirectoryChooserWithoutNeighborClasses(project);
//        directoryChooser.fillList(directories, initialDir, project, "");
//        directoryChooser.showAndGet();
        return chooser.getSelectedDirectory();
    }

    public static void createFile(Project project, @NotNull PsiPackage psiPackage, String fileName, String context, LanguageFileType fileType) {
        PsiFile psiFile=  PsiFileFactory.getInstance(project).createFileFromText(fileName, fileType, context);
        PsiDirectory psiDirectory = psiPackage.getDirectories()[0];
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

    private static DirectoryChooser createDirectoryChooserWithoutNeighborClasses(@NotNull Project project) {
        DirectoryChooser directoryChooser = new DirectoryChooser(project);

        try {
            Field myTabbedPaneWrapperField = directoryChooser.getClass().getDeclaredField("myTabbedPaneWrapper");
            myTabbedPaneWrapperField.setAccessible(true);
            ((TabbedPaneWrapper) myTabbedPaneWrapperField.get(directoryChooser)).removeTabAt(1);
        } catch (Exception e) {
            //Ignore
        }

        return directoryChooser;
    }

}
