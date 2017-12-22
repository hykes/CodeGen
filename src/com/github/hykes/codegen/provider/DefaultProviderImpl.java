package com.github.hykes.codegen.provider;

import com.github.hykes.codegen.model.CodeContext;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.utils.BuilderUtil;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.java.generate.velocity.VelocityFactory;

import java.io.StringWriter;
import java.util.Map;

/**
 * 默认文件提供者
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/04/07
 */
public class DefaultProviderImpl extends AbstractFileProvider {

    protected final static VelocityEngine VELOCITY_ENGINE = VelocityFactory.getVelocityEngine();

    private static final Logger LOGGER = Logger.getInstance(DefaultProviderImpl.class);

    protected LanguageFileType languageFileType;

    public DefaultProviderImpl(Project project, PsiDirectory psiDirectory, LanguageFileType languageFileType) {
        super(project, psiDirectory);
        this.languageFileType = languageFileType;
        /*
          IDEA 的 URLClassLoader 无法获取当前插件的 path
          @see org.apache.velocity.util.ClassUtils
         */
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        VELOCITY_ENGINE.loadDirective("com.github.hykes.codegen.directive.LowerCase");
        VELOCITY_ENGINE.loadDirective("com.github.hykes.codegen.directive.UpperCase");
        VELOCITY_ENGINE.loadDirective("com.github.hykes.codegen.directive.Append");
        VELOCITY_ENGINE.loadDirective("com.github.hykes.codegen.directive.Split");
        VELOCITY_ENGINE.loadDirective("com.github.hykes.codegen.directive.ImportPackage");
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    @Override
    public void create(CodeTemplate template, CodeContext context, Map<String, Object> extraMap) throws Exception{

        VelocityContext velocityContext = new VelocityContext(BuilderUtil.transBean2Map(context));
        velocityContext.put("serialVersionUID", BuilderUtil.computeDefaultSUID(context.getModel(), context.getFields()));
        if (extraMap != null && extraMap.size() > 0) {
            for (Map.Entry<String, Object> entry: extraMap.entrySet()) {
                velocityContext.put(entry.getKey(), entry.getValue());
            }
        }

        StringWriter fileNameWriter = new StringWriter();
        VELOCITY_ENGINE.evaluate(velocityContext, fileNameWriter, "", template.getFilename());

        StringWriter templateWriter = new StringWriter();
        VELOCITY_ENGINE.evaluate(velocityContext, templateWriter, "", template.getTemplate());

        WriteCommandAction.runWriteCommandAction(this.project, () -> {
            try {
                PsiDirectory directory = subDirectory(psiDirectory, template.getSubPath(), template.getResources());
                PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackage(directory);
                if (!Strings.isNullOrEmpty(psiPackage.getQualifiedName())) {
                    extraMap.put(fileNameWriter.toString(), new StringBuilder(psiPackage.getQualifiedName()).append(".").append(fileNameWriter.toString()));
                }
                createFile(project, directory, new StringBuilder(fileNameWriter.toString()).append(".").append(this.languageFileType.getDefaultExtension()).toString(), templateWriter.toString(), this.languageFileType);
            } catch (Exception e) {
                LOGGER.error(Throwables.getStackTraceAsString(e));
            }
        });
    }

}