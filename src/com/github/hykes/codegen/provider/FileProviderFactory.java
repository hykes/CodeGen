package com.github.hykes.codegen.provider;

import com.github.hykes.codegen.provider.filetype.KotlinFileType;
import com.github.hykes.codegen.provider.filetype.MdFileType;
import com.github.hykes.codegen.provider.filetype.SqlFileType;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.project.Project;

/**
 * 文件提供者工厂
 * @author hehaiyangwork@gmail.com
 * @date 2017/3/17
 */
public class FileProviderFactory {

    final private Project project;

    final private String outputPath;

    public FileProviderFactory(Project project, String outputPath) {
        this.project = project;
        this.outputPath = outputPath;
    }

    public AbstractFileProvider getInstance(String type) {
        if("java".equals(type)) {
            return new DefaultProviderImpl(project, outputPath, JavaFileType.INSTANCE);
        } else if("sql".equals(type)) {
            return new DefaultProviderImpl(project, outputPath, SqlFileType.INSTANCE);
        } else if("xml".equals(type)) {
            return new DefaultProviderImpl(project, outputPath, XmlFileType.INSTANCE);
        } else if("kt".equals(type)) {
            return new DefaultProviderImpl(project, outputPath, KotlinFileType.INSTANCE);
        } else {
            return new DefaultProviderImpl(project, outputPath, MdFileType.INSTANCE);
        }
    }
}