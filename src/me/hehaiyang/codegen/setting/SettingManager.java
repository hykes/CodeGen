package me.hehaiyang.codegen.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import me.hehaiyang.codegen.model.CodeTemplate;
import me.hehaiyang.codegen.setting.ui.ConfigSetting;
import me.hehaiyang.codegen.setting.ui.TemplatesSetting;
import me.hehaiyang.codegen.setting.ui.VariablesSetting;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@Data
@State(name = "CodeGen", storages = { @Storage(id = "other", file = "$APP_CONFIG$/format.xml") })
public class SettingManager implements PersistentStateComponent<SettingManager> {

    public static SettingManager getInstance() {
        return ServiceManager.getService(SettingManager.class);
    }

    private ConfigSetting configSetting;

    private VariablesSetting variablesSetting;

    private TemplatesSetting templatesSetting;

    public SettingManager() {
        this.configSetting = new ConfigSetting();
        this.variablesSetting = new VariablesSetting();
        this.templatesSetting = new TemplatesSetting();
    }

    @Nullable
    public SettingManager getState() {
        return this;
    }

    public void loadState(SettingManager formatSetting) {
        XmlSerializerUtil.copyBean(formatSetting, this);
    }

}
