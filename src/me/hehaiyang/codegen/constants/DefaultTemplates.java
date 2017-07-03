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
            List<CodeTemplate> apiTemplates = Lists.newArrayList();

            apiTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Controller", "java", "{{model}}s", getTemplateContext("/template/ControllerTemplate.hbs"), "front", false));
            CodeGroup apiGroup = new CodeGroup(UUID.randomUUID().toString(), "Api", apiTemplates);
            groups.add(apiGroup);

            List<CodeTemplate> modelTemplates = Lists.newArrayList();
            modelTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Model", "java", "{{model}}", getTemplateContext("/template/ModelTemplate.hbs"), "model", false));
            modelTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "ReadService", "java", "{{model}}ReadService", getTemplateContext("/template/ReadServiceTemplate.hbs"), "service", false));
            modelTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "WriteService", "java", "{{model}}WriteService", getTemplateContext("/template/WriteServiceTemplate.hbs"), "service", false));
            CodeGroup modelGroup = new CodeGroup(UUID.randomUUID().toString(), "Model-Service", modelTemplates);
            groups.add(modelGroup);

            List<CodeTemplate> implTemplates = Lists.newArrayList();
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Dao", "java", "{{model}}Dao", getTemplateContext("/template/DaoTemplate.hbs"), "dao", false));
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "ReadService", "java", "{{model}}ReadServiceImpl", getTemplateContext("/template/ReadServiceImplTemplate.hbs"), "service", false));
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "WriteService", "java", "{{model}}WriteServiceImpl", getTemplateContext("/template/WriteServiceImplTemplate.hbs"), "service", false));
            implTemplates.add(new CodeTemplate(UUID.randomUUID().toString(), "Mapper", "xml", "{{model}}Mapper", getTemplateContext("/template/MapperTemplate.hbs"), "mapper", true));
            CodeGroup implGroup = new CodeGroup(UUID.randomUUID().toString(), "ServiceImpl", implTemplates);
            groups.add(implGroup);

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
