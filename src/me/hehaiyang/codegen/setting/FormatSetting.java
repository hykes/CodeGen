package me.hehaiyang.codegen.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
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
    private String writeFormat;
    private String writeImplFormat;
    private String readFormat;
    private String readImplFormat;
    private String controlFormat;

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

    public String getWriteFormat() {
        return writeFormat == null ? "" : this.writeFormat;
    }

    public void setWriteFormat(String writeFormat) {
        this.writeFormat = writeFormat;
    }

    public String getWriteImplFormat() {
        return writeImplFormat == null ? "" : this.writeImplFormat;
    }

    public void setWriteImplFormat(String writeImplFormat) {
        this.writeImplFormat = writeImplFormat;
    }

    public String getReadImplFormat() {
        return readImplFormat == null ? "" : this.readImplFormat;
    }

    public void setReadImplFormat(String readImplFormat) {
        this.readImplFormat = readImplFormat;
    }

    public String getReadFormat() {
        return readFormat == null ? "" : this.readFormat;
    }

    public void setReadFormat(String readFormat) {
        this.readFormat = readFormat;
    }

    public String getControlFormat() {
        return controlFormat == null ? "" : this.controlFormat;
    }

    public void setControlFormat(String controlFormat) {
        this.controlFormat = controlFormat;
    }
}
