package me.hehaiyang.codegen.file;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import me.hehaiyang.codegen.file.impl.JavaProviderImpl;
import me.hehaiyang.codegen.file.impl.SqlProviderImpl;
import me.hehaiyang.codegen.file.impl.XmlProviderImpl;

public class FileProviderFactory {

    private static Project project;

    private static PsiDirectory psiDirectory ;

    public FileProviderFactory(Project project, PsiDirectory psiDirectory) {
        this.project = project;
        this.psiDirectory = psiDirectory;
    }

    public static FileProvider getInstance(String type) {

        if("java".equals(type)) {
            return new JavaProviderImpl(project, psiDirectory);
        } else if("sql".equals(type)) {
            return new SqlProviderImpl(project, psiDirectory);
        } else if("xml".equals(type)) {
            return new XmlProviderImpl(project, psiDirectory);
        }else{
            return new JavaProviderImpl(project, psiDirectory);
        }
    }
}