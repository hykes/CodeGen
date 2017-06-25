package me.hehaiyang.codegen.file;

import com.github.jknack.handlebars.Handlebars;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import me.hehaiyang.codegen.handlebars.HandlebarsFactory;

public abstract class FileProvider {

    public final static Handlebars handlebars = HandlebarsFactory.getInstance();

    public Project project;

    public PsiDirectory psiDirectory;

    public FileProvider(Project project, PsiDirectory psiDirectory) {
        this.project = project;
        this.psiDirectory = psiDirectory;
    }

    public abstract void create(String template, Object context, String fileName) throws Exception;
}