package me.hehaiyang.codegen.constants;

import com.google.common.collect.Lists;
import me.hehaiyang.codegen.model.CodeGroup;
import me.hehaiyang.codegen.model.CodeTemplate;
import me.hehaiyang.codegen.config.SettingManager;
import me.hehaiyang.codegen.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * Desc: 内置模版
 * Mail: hehaiyang@terminus.io
 * Date: 2017/6/29
 */
public class DefaultTemplates {

    public static List<CodeGroup> getDefaults() {
        List<CodeGroup> groups = Lists.newArrayList();
        try {

            // spring templates
            List<CodeTemplate> apiTemplates = Lists.newArrayList();

            apiTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Controller", "java", "{{model}}s", getTemplateContext("/template/spring/ControllerTemplate.hbs"), "front", false));
            CodeGroup apiGroup = new CodeGroup(UUID.randomUUID().toString(), "Api", 3, apiTemplates);
            groups.add(apiGroup);

            List<CodeTemplate> modelTemplates = Lists.newArrayList();
            modelTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Model", "java", "{{model}}", getTemplateContext("/template/spring/ModelTemplate.hbs"), "model", false));
            modelTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "ReadService", "java", "{{model}}ReadService", getTemplateContext("/template/spring/ReadServiceTemplate.hbs"), "service", false));
            modelTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "WriteService", "java", "{{model}}WriteService", getTemplateContext("/template/spring/WriteServiceTemplate.hbs"), "service", false));
            CodeGroup modelGroup = new CodeGroup(UUID.randomUUID().toString(), "Model-Service", 1, modelTemplates);
            groups.add(modelGroup);

            List<CodeTemplate> implTemplates = Lists.newArrayList();
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Dao", "java", "{{model}}Dao", getTemplateContext("/template/spring/DaoTemplate.hbs"), "dao", false));
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "ReadService", "java", "{{model}}ReadServiceImpl", getTemplateContext("/template/spring/ReadServiceImplTemplate.hbs"), "service", false));
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "WriteService", "java", "{{model}}WriteServiceImpl", getTemplateContext("/template/spring/WriteServiceImplTemplate.hbs"), "service", false));
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Mapper", "xml", "{{model}}Mapper", getTemplateContext("/template/spring/MapperTemplate.hbs"), "mapper", true));
            CodeGroup implGroup = new CodeGroup(UUID.randomUUID().toString(), "ServiceImpl", 2,  implTemplates);
            groups.add(implGroup);

            // kotlin template
            List<CodeTemplate> ktTemplates = Lists.newArrayList();
            ktTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Controller", "kt", "{{model}}s", getTemplateContext("/template/kotlin/ControllerTemplate.hbs"), "restful", false));
            ktTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Model", "kt", "{{model}}", getTemplateContext("/template/kotlin/ModelTemplate.hbs"), "model", false));
            ktTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Service", "kt", "{{model}}Service", getTemplateContext("/template/kotlin/ServiceTemplate.hbs"), "service", false));
            ktTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "ServiceImpl", "kt", "{{model}}ServiceImpl", getTemplateContext("/template/kotlin/ServiceImplTemplate.hbs"), "service/impl", false));
            CodeGroup ktApiGroup = new CodeGroup(UUID.randomUUID().toString(), "Kotlin", 5, ktTemplates);
            groups.add(ktApiGroup);

        }catch (IOException io){
            // ignore this
        }
        return groups;
    }

    private static String getTemplateContext(String path) throws IOException{
        InputStream template = SettingManager.class.getResourceAsStream(path);
        return StringUtils.stream2String(template);
    }

}
