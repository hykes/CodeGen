package me.hehaiyang.codegen.file;

import com.github.jknack.handlebars.Handlebars;
import com.google.common.base.Strings;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import me.hehaiyang.codegen.handlebars.HandlebarsFactory;

import java.util.Objects;

public abstract class FileProvider {

    public final static Handlebars handlebars = HandlebarsFactory.getInstance();

    public Project project;

    public PsiDirectory psiDirectory;

    public FileProvider(Project project, PsiDirectory psiDirectory) {
        this.project = project;
        this.psiDirectory = psiDirectory;
    }

    public abstract void create(String template, Object context, String fileName) throws Exception;

    public PsiDirectory subDirectory(PsiDirectory psiDirectory){
        return subDirectory(psiDirectory, null);
    }

    public PsiDirectory subDirectory(PsiDirectory psiDirectory, String subName){
        if(Strings.isNullOrEmpty(subName)){
            return psiDirectory;
        }else{
            return psiDirectory.createSubdirectory(subName);
        }
    }
}