package me.hehaiyang.plugins.codegen.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@State(name = "FormatSetting", storages = { @Storage(id = "other", file = "$APP_CONFIG$/format.xml") })
public class FormatSetting implements PersistentStateComponent<FormatSetting> {

    private String mapperFormat;

    private String sqlFormat;

    private String modelFormat;

    public FormatSetting() {
    }

    public static FormatSetting getInstance() {
        return ServiceManager.getService(FormatSetting.class);
    }

    @Nullable
    public FormatSetting getState() {
        return this;
    }

    public void loadState(FormatSetting formatSetting) {
        XmlSerializerUtil.copyBean(formatSetting, this);
    }

    public String getMapperFormat() {
        return this.mapperFormat == null ? "" : this.mapperFormat;
    }

    public void setMapperFormat(String mapperFormat) {
        this.mapperFormat = mapperFormat;
    }

    public String getSqlFormat() {
        return this.sqlFormat == null ? "" : this.sqlFormat;
    }

    public void setSqlFormat(String sqlFormat) {
        this.sqlFormat = sqlFormat;
    }

    public String getModelFormat() {
        return modelFormat  == null ? "" : this.modelFormat;
    }

    public void setModelFormat(String modelFormat) {
        this.modelFormat = modelFormat;
    }
}
