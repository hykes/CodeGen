package com.github.hykes.codegen.provider;

import com.github.hykes.codegen.model.CodeContext;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * 文件提供者抽象类
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/04/07
 */
public abstract class AbstractFileProvider {

    protected Project project;

    protected PsiDirectory psiDirectory;

    public AbstractFileProvider(Project project, PsiDirectory psiDirectory) {
        this.project = project;
        this.psiDirectory = psiDirectory;
    }

    public abstract void create(CodeTemplate template, CodeContext context, Map<String, Object> extraMap) throws Exception;

    protected PsiDirectory subDirectory(PsiDirectory psiDirectory, String subPath, Boolean isResources) {
        if (StringUtils.isEmpty(subPath)) {
            return psiDirectory;
        } else {
            if ("/".equals(subPath.substring(0, 1))) {
                subPath = subPath.substring(1);
            }
            String[] subPathAttr = subPath.split("/");
            if (Objects.nonNull(isResources) && isResources) {
                psiDirectory = findResourcesDirectory(psiDirectory);
            }
            return createSubDirectory(psiDirectory, subPathAttr, 0);
        }
    }

    /**
     * 创建子目录
     * @param psiDirectory
     * @param temp
     * @param level
     * @return
     */
    private PsiDirectory createSubDirectory(PsiDirectory psiDirectory, String[] temp, int level) {
        PsiDirectory subdirectory = psiDirectory.findSubdirectory(temp[level]);
        if (subdirectory == null) {
            subdirectory = psiDirectory.createSubdirectory(temp[level]);
        }
        if (temp.length != level + 1) {
            return createSubDirectory(subdirectory, temp, level + 1);
        }
        return subdirectory;
    }

    /**
     * 根据选择的package目录，找到resources目录
     * @param psiDirectory
     * @return
     */
    private PsiDirectory findResourcesDirectory(PsiDirectory psiDirectory) {

        PsiDirectory iterator = psiDirectory.getParentDirectory();

        while (iterator != null && !iterator.getName().equals("main")) {
            iterator = iterator.getParentDirectory();
        }

        PsiDirectory resourcesDirectory = iterator == null ? null : iterator.findSubdirectory("resources");
        if (resourcesDirectory == null) {
            resourcesDirectory = psiDirectory.getParentDirectory().createSubdirectory("resources");
        }
        return resourcesDirectory;
    }

    protected PsiFile createFile(Project project, @NotNull PsiDirectory psiDirectory, String fileName, String context, LanguageFileType fileType) {
        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText(fileName, fileType, context);
        // reformat class
        CodeStyleManager.getInstance(project).reformat(psiFile);
        if (psiFile instanceof PsiJavaFile) {
            JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
            styleManager.optimizeImports(psiFile);
            styleManager.shortenClassReferences(psiFile);
        }
        psiDirectory.add(psiFile);
        return psiFile;
    }

}