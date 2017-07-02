package me.hehaiyang.codegen.file.impl;

import com.github.jknack.handlebars.Template;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import me.hehaiyang.codegen.file.FileProvider;
import me.hehaiyang.codegen.utils.BuilderUtil;
import me.hehaiyang.codegen.utils.PsiUtil;

public class JavaProviderImpl extends FileProvider {

    public JavaProviderImpl(Project project, PsiDirectory psiDirectory) {
        super(project, psiDirectory);
    }

    @Override
    public void create(String template, Object context, String fileName) throws Exception{
        Template input = handlebars.compileInline(template);
        String data = input.apply(BuilderUtil.transBean2Map(context));

        Template fileNameTemp = handlebars.compileInline(fileName);
        String outputName = fileNameTemp.apply(BuilderUtil.transBean2Map(context));

        PsiUtil.createFile(project, subDirectory(psiDirectory, null), outputName + JavaFileType.DOT_DEFAULT_EXTENSION, data, JavaFileType.INSTANCE);
    }

}