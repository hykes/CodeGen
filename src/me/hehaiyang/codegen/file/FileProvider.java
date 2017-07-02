package me.hehaiyang.codegen.file;

import com.github.jknack.handlebars.Handlebars;
import com.google.common.base.Strings;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import me.hehaiyang.codegen.handlebars.HandlebarsFactory;
import me.hehaiyang.codegen.model.CodeContext;
import me.hehaiyang.codegen.model.CodeTemplate;

public abstract class FileProvider {

    public final static Handlebars handlebars = HandlebarsFactory.getInstance();

    public Project project;

    public PsiDirectory psiDirectory;

    public FileProvider(Project project, PsiDirectory psiDirectory) {
        this.project = project;
        this.psiDirectory = psiDirectory;
    }

    public abstract void create(CodeTemplate template, CodeContext context) throws Exception;

    public PsiDirectory subDirectory(PsiDirectory psiDirectory){
        return subDirectory(psiDirectory, null);
    }

    public PsiDirectory subDirectory(PsiDirectory psiDirectory, String subPath){
        if(Strings.isNullOrEmpty(subPath)){
            return psiDirectory;
        }else{
            String subPathAttr[] = subPath.split("/");
            return createSubdirectory(psiDirectory, subPathAttr, 0);
        }
    }

    private PsiDirectory createSubdirectory(PsiDirectory psiDirectory, String temp[], int level){
        PsiDirectory subdirectory = psiDirectory.findSubdirectory(temp[level]);
        if(subdirectory == null){
            subdirectory = psiDirectory.createSubdirectory(temp[level]);
        }
        if(temp.length != level + 1){
            return createSubdirectory(subdirectory, temp, level + 1);
        }
        return subdirectory;
    }
}