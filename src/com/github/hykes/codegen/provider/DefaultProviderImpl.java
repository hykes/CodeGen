package com.github.hykes.codegen.provider;

import com.github.hykes.codegen.model.CodeContext;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.utils.BuilderUtil;
import com.github.hykes.codegen.utils.StringUtils;
import com.github.hykes.codegen.utils.VelocityUtil;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import org.apache.velocity.VelocityContext;

import java.util.Map;

/**
 * 默认文件提供者
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/04/07
 */
public class DefaultProviderImpl extends AbstractFileProvider {

    private static final Logger LOGGER = Logger.getInstance(DefaultProviderImpl.class);

    protected LanguageFileType languageFileType;

    public DefaultProviderImpl(Project project, PsiDirectory psiDirectory, LanguageFileType languageFileType) {
        super(project, psiDirectory);
        this.languageFileType = languageFileType;
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

        String fileName = VelocityUtil.evaluate(velocityContext, template.getFilename());
        String temp = VelocityUtil.evaluate(velocityContext, template.getTemplate());

        WriteCommandAction.runWriteCommandAction(this.project, () -> {
            try {
                PsiDirectory directory = subDirectory(psiDirectory, template.getSubPath(), template.getResources());
                PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackage(directory);
                if (!StringUtils.isEmpty(psiPackage.getQualifiedName())) {
                    extraMap.put(fileName, new StringBuilder(psiPackage.getQualifiedName()).append(".").append(fileName));
                }
                createFile(project, directory, new StringBuilder(fileName).append(".").append(this.languageFileType.getDefaultExtension()).toString(), temp, this.languageFileType);
            } catch (Exception e) {
                LOGGER.error(StringUtils.getStackTraceAsString(e));
            }
        });
    }

}