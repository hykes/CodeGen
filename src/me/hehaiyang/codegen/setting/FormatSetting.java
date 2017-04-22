package me.hehaiyang.codegen.setting;

import com.google.common.collect.Maps;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import me.hehaiyang.codegen.model.CodeTemplate;
import me.hehaiyang.codegen.utils.ParseUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@Data
@State(name = "CodeGen", storages = { @Storage(id = "other", file = "$APP_CONFIG$/format.xml") })
public class FormatSetting implements PersistentStateComponent<FormatSetting> {

    public static FormatSetting getInstance() {
        return ServiceManager.getService(FormatSetting.class);
    }

    public FormatSetting() {
        this.loadDefaultSettings();
    }

    /**
     * 模版列表
     */
    private Map<String, CodeTemplate> codeTemplates;

    /**
     * 参数列表
     */
    private Map<String, String> params;

    /**
     * 生产方式
     * 1 markdown
     * 2 database
     */
    private Integer type;

    private static String getTemplateContext(String path) throws IOException{
        InputStream template = FormatSetting.class.getResourceAsStream(path);
        return ParseUtils.stream2String(template);
    }

    public void loadDefaultSettings() {
        Map<String, CodeTemplate> codeTemplates = Maps.newHashMap();
        try {
            String javaId = UUID.randomUUID().toString();
            String xmlId = UUID.randomUUID().toString();
            String sqlId = UUID.randomUUID().toString();
            codeTemplates.put(javaId, new CodeTemplate(javaId, "Java Template", "java", "{{model}}", getTemplateContext("/template/JavaTemplate.hbs")));
            codeTemplates.put(xmlId, new CodeTemplate(xmlId, "Xml Template", "xml", "{{model}}", getTemplateContext("/template/XmlTemplate.hbs")));
            codeTemplates.put(sqlId, new CodeTemplate(sqlId, "Sql Template", "sql", "{{model}}", getTemplateContext("/template/SqlTemplate.hbs")));

        }catch (IOException io){
            // do nothing
        }
        this.codeTemplates = codeTemplates;
        Map<String, String> params = Maps.newHashMap();
        params.put("author", "[ your name ]");
        params.put("email", "[ your email ]");
        params.put("mobile", "[ your mobile ]");
        this.params = params;
    }

    @Nullable
    public FormatSetting getState() {
        return this;
    }

    public void loadState(FormatSetting formatSetting) {
        XmlSerializerUtil.copyBean(formatSetting, this);
    }

    public CodeTemplate getCodeTemplate(String id) {
        return codeTemplates.get(id);
    }

    public void addCodeTemplate(CodeTemplate template) {
        codeTemplates.put(template.getId(), template);
    }

    public void removeCodeTemplate(String id) {
        codeTemplates.remove(id);
    }

}
