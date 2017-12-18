package com.github.hykes.codegen.configurable.instance;

import com.github.hykes.codegen.configurable.ui.TemplatesUI;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 模版配置
 *
 * @author: hehaiyangwork@qq.com
 * @date : 2017/30/17
 */
public class TemplatesConfigurable implements Configurable {

    private TemplatesUI templatesUI;

    @Override
    @Nullable
    public JComponent createComponent() {
        if(templatesUI == null) {
            templatesUI = new TemplatesUI();
        }
        return templatesUI;
    }

    @Override
    public boolean isModified() {
        return templatesUI != null && templatesUI.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        if(templatesUI != null){
            templatesUI.apply();
        }
    }

    @Override
    public void reset() {
        if(templatesUI != null){
            templatesUI.reset();
        }
    }

    @Override
    public void disposeUIResources() {
        this.templatesUI = null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Templates";
    }
}
