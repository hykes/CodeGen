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
            String sqlId = UUID.randomUUID().toString();
            List<CodeTemplate> templates = Lists.newArrayList();
            templates.add(new CodeTemplate(javaId, "Java Template", "java", "{{model}}", getTemplateContext("/template/JavaTemplate.hbs")));
            templates.add(new CodeTemplate(xmlId, "Xml Template", "xml", "{{model}}", getTemplateContext("/template/XmlTemplate.hbs")));
            templates.add( new CodeTemplate(sqlId, "Sql Template", "sql", "{{model}}", getTemplateContext("/template/SqlTemplate.hbs")));
            String groupId = UUID.randomUUID().toString();
            CodeGroup group = new CodeGroup(groupId, "default", 1, templates);
            groups.add(group);

        }catch (IOException io){
        // do nothing
        }
    }

    private static String getTemplateContext(String path) throws IOException{
      InputStream template = SettingManager.class.getResourceAsStream(path);
      return ParseUtils.stream2String(template);
    }

    public Map<String, List<CodeTemplate>> getTemplatesMap(List<CodeGroup> groups){
        Map<String, List<CodeTemplate>> result = Maps.newHashMap();
        groups.forEach( it -> result.put(it.getId(), it.getTemplates()));
        return result;
    }

}
