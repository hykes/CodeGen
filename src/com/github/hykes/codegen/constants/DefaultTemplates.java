package com.github.hykes.codegen.constants;

import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.model.CodeGroup;
import com.github.hykes.codegen.configurable.SettingManager;
import com.github.hykes.codegen.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 内置模版
 * @author: hehaiyangwork@gmail.com
 * @date: 2017/6/29
 */
public class DefaultTemplates {

    public static List<CodeGroup> getDefaults() {
        List<CodeGroup> groups = new ArrayList<>();
        try {

            // spring templates
            List<CodeTemplate> apiTemplates = new ArrayList<>();
            apiTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Controller", "java", "${model}s", getTemplateContext("/template/spring/ControllerTemplate.vm"), "front", false));
            CodeGroup apiGroup = new CodeGroup(UUID.randomUUID().toString(), "Api", 3, apiTemplates);
            groups.add(apiGroup);

            List<CodeTemplate> modelTemplates = new ArrayList<>();
            modelTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Model", "java", "${model}", getTemplateContext("/template/spring/ModelTemplate.vm"), "model", false));
            modelTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "ReadService", "java", "${model}ReadService", getTemplateContext("/template/spring/ReadServiceTemplate.vm"), "service", false));
            modelTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "WriteService", "java", "${model}WriteService", getTemplateContext("/template/spring/WriteServiceTemplate.vm"), "service", false));
            CodeGroup modelGroup = new CodeGroup(UUID.randomUUID().toString(), "Model-Service", 1, modelTemplates);
            groups.add(modelGroup);

            List<CodeTemplate> implTemplates = new ArrayList<>();
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Dao", "java", "${model}Dao", getTemplateContext("/template/spring/DaoTemplate.vm"), "dao", false));
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "ReadService", "java", "${model}ReadServiceImpl", getTemplateContext("/template/spring/ReadServiceImplTemplate.vm"), "service", false));
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "WriteService", "java", "${model}WriteServiceImpl", getTemplateContext("/template/spring/WriteServiceImplTemplate.vm"), "service", false));
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Mapper", "xml", "${model}Mapper", getTemplateContext("/template/spring/MapperTemplate.vm"), "mapper", true));
            CodeGroup implGroup = new CodeGroup(UUID.randomUUID().toString(), "ServiceImpl", 2,  implTemplates);
            groups.add(implGroup);

            List<CodeTemplate> testTemplates = new ArrayList<>();
            testTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "DaoTest", "java", "${model}DaoTest", getTemplateContext("/template/spring/DaoTest.vm"), "dao", false));
            CodeGroup testGroup = new CodeGroup(UUID.randomUUID().toString(), "DaoTest", 4, testTemplates);
            groups.add(testGroup);

            // kotlin template
            List<CodeTemplate> ktTemplates = new ArrayList<>();
            ktTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Controller", "kt", "${model}s", getTemplateContext("/template/kotlin/ControllerTemplate.vm"), "restful", false));
            ktTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Model", "kt", "${model}", getTemplateContext("/template/kotlin/ModelTemplate.vm"), "model", false));
            ktTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Service", "kt", "${model}Service", getTemplateContext("/template/kotlin/ServiceTemplate.vm"), "service", false));
            ktTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "ServiceImpl", "kt", "${model}ServiceImpl", getTemplateContext("/template/kotlin/ServiceImplTemplate.vm"), "service/impl", false));
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
