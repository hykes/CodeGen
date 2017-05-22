package me.hehaiyang.codegen.setting.ui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
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

    private Map<String, List<CodeTemplate>> codeTemplateTree = Maps.newHashMap();

    public TemplatesSetting() {
        try {
            String javaId = UUID.randomUUID().toString();
            String xmlId = UUID.randomUUID().toString();
            String sqlId = UUID.randomUUID().toString();
            List<CodeTemplate> codeTemplates = Lists.newArrayList();
            codeTemplates.add(new CodeTemplate(javaId, "Java Template", "java", "{{model}}", getTemplateContext("/template/JavaTemplate.hbs")));
            codeTemplates.add(new CodeTemplate(xmlId, "Xml Template", "xml", "{{model}}", getTemplateContext("/template/XmlTemplate.hbs")));
            codeTemplates.add( new CodeTemplate(sqlId, "Sql Template", "sql", "{{model}}", getTemplateContext("/template/SqlTemplate.hbs")));
            codeTemplateTree.put("default", codeTemplates);

        }catch (IOException io){
        // do nothing
        }
    }

    private static String getTemplateContext(String path) throws IOException{
      InputStream template = SettingManager.class.getResourceAsStream(path);
      return ParseUtils.stream2String(template);
    }

    public Integer getCount(){
        Integer count = 0;
        for (String pStr : codeTemplateTree.keySet()) {
            count += 1;
            count += codeTemplateTree.get(pStr).size();
        }
        return count;
    }
}
