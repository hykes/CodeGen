package com.github.hykes.codegen.configurable;

import com.github.hykes.codegen.configurable.model.Templates;
import com.github.hykes.codegen.configurable.model.Variables;
import com.github.hykes.codegen.constants.Defaults;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

/**
 * 插件配置管理
 *
 * @author hehaiyangwork@gmail.com
 * @date 2017/3/17
 */
@State(name = "CodeGen", storages = { @Storage(value = "codegen.xml") })
public class SettingManager implements PersistentStateComponent<SettingManager> {

    public static SettingManager getInstance() {
        return ServiceManager.getService(SettingManager.class);
    }

    private Variables variables;

    private Templates templates;

    public SettingManager() {
        this.variables = new Variables();
        this.templates = new Templates();
        init();
    }


    @Override
    public void loadState(SettingManager formatSetting) {
        XmlSerializerUtil.copyBean(formatSetting, this);
        init();
    }

    /**
     * 初始化配置, 防止第一次使用插件（没有生成codegen.xml）不执行{@link #loadState(SettingManager)}函数
     */
    private void init() {
        if (templates != null && templates.getRoots().size() == 0) {
            templates.setRoots(Defaults.getDefaultTemplates());
        }
        if (variables != null && StringUtils.isBlank(variables.getIgnoreFields())) {
            variables.setIgnoreFields("id,created_at,updated_at");
        }
    }


    @Override
    @Nullable
    public SettingManager getState() {
        return this;
    }

    public Variables getVariables() {
        return variables;
    }

    public void setVariables(Variables variables) {
        this.variables = variables;
    }

    public Templates getTemplates() {
        return templates;
    }

    public void setTemplates(Templates templates) {
        this.templates = templates;
    }

}
