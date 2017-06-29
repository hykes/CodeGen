package me.hehaiyang.codegen.constants;

import com.google.common.collect.Lists;
import me.hehaiyang.codegen.model.CodeGroup;
import me.hehaiyang.codegen.model.CodeTemplate;
import me.hehaiyang.codegen.config.SettingManager;
import me.hehaiyang.codegen.utils.ParseUtils;

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
            List<CodeTemplate> apiTemplates = Lists.newArrayList();

            apiTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Controller", "java", "{{model}}s", getTemplateContext("/template/ControllerTemplate.hbs")));
            CodeGroup apiGroup = new CodeGroup(UUID.randomUUID().toString(), "Api", 1, apiTemplates);
            groups.add(apiGroup);

            List<CodeTemplate> modelTemplates = Lists.newArrayList();
            modelTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Model", "java", "{{model}}", getTemplateContext("/template/ModelTemplate.hbs")));
            modelTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "ReadService", "java", "{{model}}ReadService", getTemplateContext("/template/ReadServiceTemplate.hbs")));
            modelTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "WriteService", "java", "{{model}}WriteService", getTemplateContext("/template/WriteServiceTemplate.hbs")));
            CodeGroup modelGroup = new CodeGroup(UUID.randomUUID().toString(), "Model-Service", 1, modelTemplates);
            groups.add(modelGroup);

            List<CodeTemplate> implTemplates = Lists.newArrayList();
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Dao", "java", "{{model}}Dao", getTemplateContext("/template/DaoTemplate.hbs")));
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "ReadService", "java", "{{model}}ReadServiceImpl", getTemplateContext("/template/ReadServiceImplTemplate.hbs")));
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "WriteService", "java", "{{model}}WriteServiceImpl", getTemplateContext("/template/WriteServiceImplTemplate.hbs")));
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Mapper", "xml", "{{model}}Mapper", getTemplateContext("/template/MapperTemplate.hbs")));
            CodeGroup implGroup = new CodeGroup(UUID.randomUUID().toString(), "ServiceImpl", 1, implTemplates);
            groups.add(implGroup);

        }catch (IOException io){
            // ignore this
        }
        return groups;
    }

    private static String getTemplateContext(String path) throws IOException{
        InputStream template = SettingManager.class.getResourceAsStream(path);
        return ParseUtils.stream2String(template);
    }

}
