package com.github.hykes.codegen.provider;

import com.github.hykes.codegen.provider.filetype.KotlinFileType;
import com.github.hykes.codegen.provider.filetype.SqlFileType;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

/**
 * 文件提供者工厂
 * @author: hehaiyangwork@qq.com
 * @date: 2017/3/17
 */
public class FileProviderFactory {

    final private Project project;

    final private PsiDirectory psiDirectory;

    public FileProviderFactory(Project project, PsiDirectory psiDirectory) {
        this.project = project;
        this.psiDirectory = psiDirectory;
    }

    public static FileProviderFactory create(Project project, PsiDirectory psiDirectory){
        return new FileProviderFactory(project, psiDirectory);
    }

    public AbstractFileProvider getInstance(String type) {
        if("java".equals(type)) {
            return new DefaultProviderImpl(project, psiDirectory, JavaFileType.INSTANCE);
        } else if("sql".equals(type)) {
            return new DefaultProviderImpl(project, psiDirectory, SqlFileType.INSTANCE);
        } else if("xml".equals(type)) {
            return new DefaultProviderImpl(project, psiDirectory, XmlFileType.INSTANCE);
        } else if("kt".equals(type)) {
            return new DefaultProviderImpl(project, psiDirectory, KotlinFileType.INSTANCE);
        } else {
            return new DefaultProviderImpl(project, psiDirectory, JavaFileType.INSTANCE);
        }
    }
}