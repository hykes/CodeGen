package me.hehaiyang.codegen.file;

import com.github.jknack.handlebars.Handlebars;
import com.intellij.openapi.actionSystem.AnActionEvent;
import me.hehaiyang.codegen.handlebars.HandlebarsFactory;

public abstract class FileProvider {

    public final static Handlebars handlebars = HandlebarsFactory.getInstance();

    public AnActionEvent anActionEvent;

    public FileProvider(AnActionEvent anActionEvent) {
        this.anActionEvent = anActionEvent;
    }

    public abstract void create(String template, Object context, String fileName) throws Exception;
}