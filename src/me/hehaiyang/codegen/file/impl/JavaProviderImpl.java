package me.hehaiyang.codegen.file.impl;

import com.github.jknack.handlebars.Template;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import me.hehaiyang.codegen.file.FileProvider;
import me.hehaiyang.codegen.utils.BuilderUtil;
import me.hehaiyang.codegen.utils.PsiUtil;

public class JavaProviderImpl extends FileProvider {

    public JavaProviderImpl(AnActionEvent anActionEvent) {
        super(anActionEvent);
    }

    @Override
    public void create(String template, Object context, String fileName) throws Exception{
        Template input = handlebars.compileInline(template);
        String controlContext = input.apply(BuilderUtil.transBean2Map(context));
        PsiUtil.createFile(anActionEvent, fileName + JavaFileType.DOT_DEFAULT_EXTENSION, controlContext, JavaFileType.INSTANCE);
    }

}