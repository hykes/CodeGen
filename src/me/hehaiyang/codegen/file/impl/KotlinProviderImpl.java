package me.hehaiyang.codegen.file.impl;

import com.github.jknack.handlebars.Template;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import me.hehaiyang.codegen.ext.KotlinFileType;
import me.hehaiyang.codegen.file.FileProvider;
import me.hehaiyang.codegen.model.CodeContext;
import me.hehaiyang.codegen.model.CodeTemplate;
import me.hehaiyang.codegen.utils.BuilderUtil;
import me.hehaiyang.codegen.utils.PsiUtil;

public class KotlinProviderImpl extends FileProvider {

    public KotlinProviderImpl(Project project, PsiDirectory psiDirectory) {
        super(project, psiDirectory);
    }

    @Override
    public void create(CodeTemplate template, CodeContext context) throws Exception{
        Template input = handlebars.compileInline(template.getTemplate());
        String data = input.apply(BuilderUtil.transBean2Map(context));

        Template fileNameTemp = handlebars.compileInline(template.getFilename());
        String outputName = fileNameTemp.apply(BuilderUtil.transBean2Map(context));

        PsiDirectory directory = subDirectory(psiDirectory, template.getSubPath(), template.getResources());
        PsiUtil.createFile(project, directory, outputName + KotlinFileType.DOT_DEFAULT_EXTENSION, data, KotlinFileType.INSTANCE);
    }

}