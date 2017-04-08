package me.hehaiyang.codegen.setting;

import com.google.common.collect.Maps;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import me.hehaiyang.codegen.constants.DefaultFileType;
import me.hehaiyang.codegen.constants.DefaultTemplate;
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
            codeTemplates.put(DefaultTemplate.MODEL, new CodeTemplate(DefaultTemplate.MODEL, DefaultFileType.JAVA, "{{model}}", template("/template/ModelTemplate.hbs")));

            codeTemplates.put(DefaultTemplate.CONTROLLER, new CodeTemplate(DefaultTemplate.CONTROLLER, DefaultFileType.JAVA, "{{model}}s", template("/template/ControllerTemplate.hbs")));
            codeTemplates.put(DefaultTemplate.DAO, new CodeTemplate(DefaultTemplate.DAO, DefaultFileType.JAVA, "{{model}}Dao", template("/template/DaoTemplate.hbs")));
            codeTemplates.put(DefaultTemplate.MAPPER, new CodeTemplate(DefaultTemplate.MAPPER, DefaultFileType.XML, "{{model}}Mapper", template("/template/MapperTemplate.hbs")));
            codeTemplates.put(DefaultTemplate.SQL, new CodeTemplate(DefaultTemplate.SQL, DefaultFileType.SQL, "{{model}}Schema", template("/template/SqlTemplate.hbs")));

            codeTemplates.put(DefaultTemplate.READ_SERVICE, new CodeTemplate(DefaultTemplate.READ_SERVICE, DefaultFileType.JAVA, "{{model}}ReadService", template("/template/ReadServiceTemplate.hbs")));
            codeTemplates.put(DefaultTemplate.READ_SERVICE_IMPL, new CodeTemplate(DefaultTemplate.READ_SERVICE_IMPL, DefaultFileType.JAVA, "{{model}}ReadServiceImpl", template("/template/ReadServiceImplTemplate.hbs")));
            codeTemplates.put(DefaultTemplate.WRITE_SERVICE, new CodeTemplate(DefaultTemplate.WRITE_SERVICE, DefaultFileType.JAVA, "{{model}}WriteService", template("/template/WriteServiceTemplate.hbs")));
            codeTemplates.put(DefaultTemplate.WRITE_SERVICE_IMPL, new CodeTemplate(DefaultTemplate.WRITE_SERVICE_IMPL, DefaultFileType.JAVA, "{{model}}WriteServiceImpl", template("/template/WriteServiceImplTemplate.hbs")));

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
