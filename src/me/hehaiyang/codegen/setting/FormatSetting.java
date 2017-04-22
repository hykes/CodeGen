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
import java.util.UUID;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@Data
@State(name = "ss", storages = { @Storage(id = "other", file = "$APP_CONFIG$/format.xml") })
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
//            codeTemplates.put(DefaultTemplate.MODEL, new CodeTemplate(UUID.randomUUID().toString(), DefaultTemplate.MODEL, DefaultFileType.JAVA, "{{model}}", getTemplateContext("/template/ModelTemplate.hbs")));
//            codeTemplates.put(DefaultTemplate.CONTROLLER, new CodeTemplate(UUID.randomUUID().toString(), DefaultTemplate.CONTROLLER, DefaultFileType.JAVA, "{{model}}s", getTemplateContext("/template/ControllerTemplate.hbs")));

            String daoId = UUID.randomUUID().toString();
            String mapperId = UUID.randomUUID().toString();
            String sqlId = UUID.randomUUID().toString();
            codeTemplates.put(daoId, new CodeTemplate(daoId, DefaultTemplate.DAO, DefaultFileType.JAVA, "{{model}}Dao", getTemplateContext("/template/DaoTemplate.hbs")));
            codeTemplates.put(mapperId, new CodeTemplate(mapperId, DefaultTemplate.MAPPER, DefaultFileType.XML, "{{model}}Mapper", getTemplateContext("/template/MapperTemplate.hbs")));
            codeTemplates.put(sqlId, new CodeTemplate(sqlId, DefaultTemplate.SQL, DefaultFileType.SQL, "{{model}}Schema", getTemplateContext("/template/SqlTemplate.hbs")));
//
//            codeTemplates.put(DefaultTemplate.READ_SERVICE, new CodeTemplate(UUID.randomUUID().toString(), DefaultTemplate.READ_SERVICE, DefaultFileType.JAVA, "{{model}}ReadService", getTemplateContext("/template/ReadServiceTemplate.hbs")));
//            codeTemplates.put(DefaultTemplate.READ_SERVICE_IMPL, new CodeTemplate(UUID.randomUUID().toString(), DefaultTemplate.READ_SERVICE_IMPL, DefaultFileType.JAVA, "{{model}}ReadServiceImpl", getTemplateContext("/template/ReadServiceImplTemplate.hbs")));
//            codeTemplates.put(DefaultTemplate.WRITE_SERVICE, new CodeTemplate(UUID.randomUUID().toString(), DefaultTemplate.WRITE_SERVICE, DefaultFileType.JAVA, "{{model}}WriteService", getTemplateContext("/template/WriteServiceTemplate.hbs")));
//            codeTemplates.put(DefaultTemplate.WRITE_SERVICE_IMPL, new CodeTemplate(UUID.randomUUID().toString(), DefaultTemplate.WRITE_SERVICE_IMPL, DefaultFileType.JAVA, "{{model}}WriteServiceImpl", getTemplateContext("/template/WriteServiceImplTemplate.hbs")));

        }catch (IOException io){
            // do nothing
        }

        this.codeTemplates = codeTemplates;
        Map<String, String> params = Maps.newHashMap();
        params.put("email", "[ your email ]");
        params.put("author", "[ your name ]");
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
