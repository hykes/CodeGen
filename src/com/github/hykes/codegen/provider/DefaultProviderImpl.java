package com.github.hykes.codegen.provider;

import com.github.hykes.codegen.model.CodeContext;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.utils.BuilderUtil;
import com.github.hykes.codegen.utils.PsiUtil;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.java.generate.velocity.VelocityFactory;

import java.io.StringWriter;

/**
 * 默认文件提供者
 *
 * @author: hehaiyangwork@gmail.com
 * @date: 2017/04/07
 */
public class DefaultProviderImpl extends AbstractFileProvider {

    protected final static VelocityEngine velocityEngine = VelocityFactory.getVelocityEngine();

    protected LanguageFileType languageFileType;

    public DefaultProviderImpl(Project project, PsiDirectory psiDirectory, LanguageFileType languageFileType) {
        super(project, psiDirectory);
        this.languageFileType = languageFileType;
        velocityEngine.setProperty("userdirective", "com.github.hykes.codegen.directive.Append");
    }

    @Override
    public void create(CodeTemplate template, CodeContext context) throws Exception{

        VelocityContext velocityContext = new VelocityContext(BuilderUtil.transBean2Map(context));

        StringWriter templateWriter = new StringWriter();
        velocityEngine.evaluate(velocityContext, templateWriter, "", template.getTemplate());

        StringWriter fileNameWriter = new StringWriter();
        velocityEngine.evaluate(velocityContext, fileNameWriter, "", template.getFilename());

        PsiDirectory directory = subDirectory(psiDirectory, template.getSubPath(), template.getResources());
        PsiUtil.createFile(project, directory, fileNameWriter.toString() + this.languageFileType.getDefaultExtension(), templateWriter.toString(), this.languageFileType);
    }

}