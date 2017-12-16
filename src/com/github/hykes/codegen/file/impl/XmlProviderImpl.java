package com.github.hykes.codegen.file.impl;

import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.utils.PsiUtil;
import com.github.jknack.handlebars.Template;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.github.hykes.codegen.file.AbstractFileProvider;
import com.github.hykes.codegen.model.CodeContext;
import com.github.hykes.codegen.utils.BuilderUtil;

public class XmlProviderImpl extends AbstractFileProvider {

    public XmlProviderImpl(Project project, PsiDirectory psiDirectory) {
        super(project, psiDirectory);
    }

    @Override
    public void create(CodeTemplate template, CodeContext context) throws Exception{
        Template input = HANDLEBARS.compileInline(template.getTemplate());
        String data = input.apply(BuilderUtil.transBean2Map(context));

        Template fileNameTemp = HANDLEBARS.compileInline(template.getFilename());
        String outputName = fileNameTemp.apply(BuilderUtil.transBean2Map(context));

        PsiDirectory directory = subDirectory(psiDirectory, template.getSubPath(), template.getResources());
        PsiUtil.createFile(project, directory, outputName + XmlFileType.DOT_DEFAULT_EXTENSION, data, XmlFileType.INSTANCE);
    }

}