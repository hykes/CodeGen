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

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@Data
@State(name = "FormatSetting", storages = { @Storage(id = "other", file = "$APP_CONFIG$/format.xml") })
public class FormatSetting implements PersistentStateComponent<FormatSetting> {

    public static FormatSetting getInstance() {
        return ServiceManager.getService(FormatSetting.class);
    }

    private Map<String, CodeTemplate> codeTemplates;

    private Map<String, String> params;

    public FormatSetting() {
        loadDefaultSettings();
    }

    private static String template(String path) throws IOException{
        InputStream template = FormatSetting.class.getResourceAsStream(path);
        return ParseUtils.stream2String(template);
    }

    public void loadDefaultSettings() {
        Map<String, CodeTemplate> codeTemplates = Maps.newHashMap();
        try {
            codeTemplates.put("model", new CodeTemplate("model", "java", "{{model}}", template("/template/ModelTemplate.hbs")));

            codeTemplates.put("Controller", new CodeTemplate("Controller", "java", "{{Controller}}", template("/template/ControllerTemplate.hbs")));
            codeTemplates.put("Dao", new CodeTemplate("Dao", "java", "{{Dao}}", template("/template/DaoTemplate.hbs")));
            codeTemplates.put("Mapper", new CodeTemplate("Mapper", "xml", "{{Mapper}}", template("/template/MapperTemplate.hbs")));
            codeTemplates.put("Sql", new CodeTemplate("Sql", "sql", "{{Sql}}", template("/template/SqlTemplate.hbs")));

            codeTemplates.put("ReadService", new CodeTemplate("ReadService", "java", "{{ReadService}}", template("/template/ReadServiceTemplate.hbs")));
            codeTemplates.put("ReadServiceImpl", new CodeTemplate("ReadServiceImpl", "java", "{{ReadServiceImpl}}", template("/template/ReadServiceImplTemplate.hbs")));
            codeTemplates.put("WriteService", new CodeTemplate("WriteService", "java", "{{WriteService}}", template("/template/WriteServiceTemplate.hbs")));
            codeTemplates.put("WriteServiceImpl", new CodeTemplate("WriteServiceImpl", "java", "{{WriteService}}", template("/template/WriteServiceImplTemplate.hbs")));

        }catch (IOException io){
            // do nothing
        }

        this.codeTemplates = codeTemplates;

        Map<String, String> params = Maps.newHashMap();
        params.put("email", "[ your email ]");
        params.put("author", "[ your name ]");
        this.params = params;
    }

    @Nullable
    public FormatSetting getState() {
        return this;
    }

    public void loadState(FormatSetting formatSetting) {
        XmlSerializerUtil.copyBean(formatSetting, this);
    }

    public CodeTemplate getCodeTemplate(String template) {
        return codeTemplates.get(template);
    }

    public void removeCodeTemplate(String template) {
        codeTemplates.remove(template);
    }

}
