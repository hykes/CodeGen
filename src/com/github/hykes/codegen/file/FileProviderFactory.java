package com.github.hykes.codegen.file;

import com.github.hykes.codegen.file.impl.JavaProviderImpl;
import com.github.hykes.codegen.file.impl.KotlinProviderImpl;
import com.github.hykes.codegen.file.impl.SqlProviderImpl;
import com.github.hykes.codegen.file.impl.XmlProviderImpl;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

public class FileProviderFactory {

    private static Project project;

    private static PsiDirectory psiDirectory ;

    public FileProviderFactory(Project project, PsiDirectory psiDirectory) {
        this.project = project;
        this.psiDirectory = psiDirectory;
    }

    public static FileProviderFactory create(Project project, PsiDirectory psiDirectory){
        return new FileProviderFactory(project,psiDirectory);
    }

    public static FileProvider getInstance(String type) {

        if("java".equals(type)) {
            return new JavaProviderImpl(project, psiDirectory);
        } else if("sql".equals(type)) {
            return new SqlProviderImpl(project, psiDirectory);
        } else if("xml".equals(type)) {
            return new XmlProviderImpl(project, psiDirectory);
        } else if("kt".equals(type)) {
            return new KotlinProviderImpl(project, psiDirectory);
        } else
            return new JavaProviderImpl(project, psiDirectory);
    }
}