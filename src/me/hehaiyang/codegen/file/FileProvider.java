package me.hehaiyang.codegen.file;

import com.github.jknack.handlebars.Handlebars;
import com.intellij.ide.IdeView;
import com.intellij.openapi.project.Project;
import me.hehaiyang.codegen.handlebars.HandlebarsFactory;

public abstract class FileProvider {

    public final static Handlebars handlebars = HandlebarsFactory.getInstance();

    public Project project;

    public IdeView ideView;

    public FileProvider(Project project, IdeView ideView) {
        this.project = project;
        this.ideView = ideView;
    }

    public abstract void create(String template, Object context, String fileName) throws Exception;
}