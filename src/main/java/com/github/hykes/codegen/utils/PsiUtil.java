package com.github.hykes.codegen.utils;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Desc:
 * Mail: hehaiyangwork@gmail.com
 * Date: 2017/4/6
 */
public class PsiUtil {

    private static final Logger LOGGER = Logger.getInstance(PsiUtil.class);

    public static Project getProject(AnActionEvent anActionEvent) {
        return anActionEvent.getData(PlatformDataKeys.PROJECT);
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

    public static PsiDirectory createDirectory(Project project, String title, String description) {
        final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        descriptor.setTitle(title);
        descriptor.setShowFileSystemRoots(false);
        descriptor.setDescription(description);
        descriptor.setHideIgnored(true);
        descriptor.setRoots(ProjectRootManager.getInstance(project).getContentRoots());
        descriptor.setForcedToUseIdeaFileChooser(true);
        VirtualFile file = FileChooser.chooseFile(descriptor, project, project.getProjectFile());
        if(Objects.isNull(file)){
            Messages.showInfoMessage("Cancel " + title, "Error");
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

    public static VirtualFile chooseFolder(@Nullable Project project, String title, String description, boolean showFileSystemRoots, boolean hideIgnored, @Nullable VirtualFile toSelect){
        final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        descriptor.setTitle(title);
        descriptor.setDescription(description);
        descriptor.setShowFileSystemRoots(showFileSystemRoots);
        descriptor.setHideIgnored(hideIgnored);
        return FileChooser.chooseFile(descriptor, project, toSelect);
    }

    public static VirtualFile chooseFile(@Nullable Project project, String title, String description, boolean showFileSystemRoots, boolean hideIgnored, @Nullable VirtualFile toSelect){
        final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
        descriptor.setTitle(title);
        descriptor.setDescription(description);
        descriptor.setShowFileSystemRoots(showFileSystemRoots);
        descriptor.setHideIgnored(hideIgnored);
        return FileChooser.chooseFile(descriptor, project, toSelect);
    }

}
