package com.github.hykes.codegen.file.impl;

import com.github.hykes.codegen.file.FileProvider;
import com.github.hykes.codegen.model.CodeContext;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.utils.PsiUtil;
import com.github.jknack.handlebars.Template;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.github.hykes.codegen.utils.BuilderUtil;

public class JavaProviderImpl extends FileProvider {

    public JavaProviderImpl(Project project, PsiDirectory psiDirectory) {
        super(project, psiDirectory);
    }

    @Override
    public void create(CodeTemplate template, CodeContext context) throws Exception{
        Template input = handlebars.compileInline(template.getTemplate());
        String data = input.apply(BuilderUtil.transBean2Map(context));

        Template fileNameTemp = handlebars.compileInline(template.getFilename());
        String outputName = fileNameTemp.apply(BuilderUtil.transBean2Map(context));

        PsiDirectory directory = subDirectory(psiDirectory, template.getSubPath(), template.getResources());
        PsiUtil.createFile(project, directory, outputName + JavaFileType.DOT_DEFAULT_EXTENSION, data, JavaFileType.INSTANCE);
    }

}