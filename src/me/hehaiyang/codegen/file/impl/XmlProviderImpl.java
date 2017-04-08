package me.hehaiyang.codegen.file.impl;

import com.github.jknack.handlebars.Template;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import me.hehaiyang.codegen.file.FileProvider;
import me.hehaiyang.codegen.utils.BuilderUtil;
import me.hehaiyang.codegen.utils.PsiUtil;

public class XmlProviderImpl extends FileProvider {

    public XmlProviderImpl(AnActionEvent anActionEvent) {
        super(anActionEvent);
    }

    @Override
    public void create(String template, Object context, String fileName) throws Exception {
        Template input = handlebars.compileInline(template);
        String data = input.apply(BuilderUtil.transBean2Map(context));

        Template fileNameTemp = handlebars.compileInline(fileName);
        String outputName = fileNameTemp.apply(BuilderUtil.transBean2Map(context));

        PsiUtil.createFile(anActionEvent, outputName + XmlFileType.DOT_DEFAULT_EXTENSION, data, XmlFileType.INSTANCE);
    }

}