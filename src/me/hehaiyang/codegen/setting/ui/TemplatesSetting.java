package me.hehaiyang.codegen.setting.ui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import me.hehaiyang.codegen.model.CodeGroup;
import me.hehaiyang.codegen.model.CodeTemplate;
import me.hehaiyang.codegen.setting.SettingManager;
import me.hehaiyang.codegen.utils.ParseUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Desc:
 * Mail: hehaiyang@terminus.io
 * Date: 2017/5/10
 */
@Data
public class TemplatesSetting {

    private List<CodeGroup> groups = Lists.newArrayList();

    public TemplatesSetting() {
        try {
            String javaId = UUID.randomUUID().toString();
            String xmlId = UUID.randomUUID().toString();
            List<CodeTemplate> templates = Lists.newArrayList();
            templates.add(new CodeTemplate(javaId, "Controller", "java", "{{model}}s", getTemplateContext("/template/ControllerTemplate.hbs")));
            templates.add(new CodeTemplate(xmlId, "Model", "java", "{{model}}", getTemplateContext("/template/ModelTemplate.hbs")));
            String groupId = UUID.randomUUID().toString();
            CodeGroup group = new CodeGroup(groupId, "default", 1, templates);
            groups.add(group);

            String daoId = UUID.randomUUID().toString();
            String mapperId = UUID.randomUUID().toString();
            List<CodeTemplate> templates2 = Lists.newArrayList();
            templates2.add(new CodeTemplate(daoId, "Dao", "java", "{{model}}Dao", getTemplateContext("/template/DaoTemplate.hbs")));
            templates2.add(new CodeTemplate(mapperId, "Mapper", "xml", "{{model}}Mapper", getTemplateContext("/template/MapperTemplate.hbs")));
            String groupId2 = UUID.randomUUID().toString();
            CodeGroup group2 = new CodeGroup(groupId2, "default2", 2, templates);
            groups.add(group2);

        }catch (IOException io){
        // do nothing
        }
    }

    private static String getTemplateContext(String path) throws IOException{
      InputStream template = SettingManager.class.getResourceAsStream(path);
      return ParseUtils.stream2String(template);
    }

    public Map<String, List<CodeTemplate>> getTemplatesMap(){
        Map<String, List<CodeTemplate>> result = Maps.newHashMap();
        groups.forEach( it -> result.put(it.getId(), it.getTemplates()));
        return result;
    }

}
