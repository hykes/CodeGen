package me.hehaiyang.codegen.setting.configurable;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import me.hehaiyang.codegen.setting.ui.DatabasesUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Desc: 数据库
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/5/31
 */
public class DatabasesSettingConfigurable implements SearchableConfigurable {

    private DatabasesUI databasesUI;

    @NotNull
    public String getId() {
        return "codeGen.databases";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nls
    public String getDisplayName() {
        return this.getId();
    }

    @Nullable
    public String getHelpTopic() {
        return this.getId();
    }

    @Nullable
    public JComponent createComponent() {
        if(databasesUI == null) {
            databasesUI = new DatabasesUI();
        }
        return databasesUI;
    }

    public boolean isModified() {
        return databasesUI != null && databasesUI.isModified();
    }

    public void apply() throws ConfigurationException {
        if(databasesUI != null){
            databasesUI.apply();
        }
    }

    public void reset() {
        if(databasesUI != null){
            databasesUI.reset();
        }
    }

    @Override
    public void disposeUIResources() {
        this.databasesUI = null;
    }

}
