package com.github.hykes.codegen.file.impl;

import com.github.hykes.codegen.file.AbstractFileProvider;
import com.github.hykes.codegen.file.filetype.SqlFileType;
import com.github.hykes.codegen.model.CodeContext;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.utils.PsiUtil;
import com.github.jknack.handlebars.Template;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.github.hykes.codegen.utils.BuilderUtil;
import org.apache.velocity.VelocityContext;

import java.io.StringWriter;
import java.util.Map;

public class SqlProviderImpl extends AbstractFileProvider {

    public SqlProviderImpl(Project project, PsiDirectory psiDirectory) {
        super(project, psiDirectory);
    }

    @Override
    public void create(CodeTemplate template, CodeContext context) throws Exception{
        Map<String, Object> map = BuilderUtil.transBean2Map(context);

        Template input = HANDLEBARS.compileInline(template.getTemplate());
        String data = input.apply(map);

        StringWriter fileNameWriter = new StringWriter();
        velocityEngine.evaluate(new VelocityContext(map), fileNameWriter, "", template.getFilename());

        PsiDirectory directory = subDirectory(psiDirectory, template.getSubPath(), template.getResources());
        PsiUtil.createFile(project, directory, fileNameWriter.toString() + SqlFileType.DOT_DEFAULT_EXTENSION, data, SqlFileType.INSTANCE);
    }

}