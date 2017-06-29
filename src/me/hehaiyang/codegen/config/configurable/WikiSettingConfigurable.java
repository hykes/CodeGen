package me.hehaiyang.codegen.config.configurable;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import me.hehaiyang.codegen.config.ui.WikiTextAreaUI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Desc: wiki
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
public class WikiSettingConfigurable implements SearchableConfigurable {

    private WikiTextAreaUI wikiTextAreaUI;

    @NotNull
    public String getId() {
        return "codeGen.wiki";
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
        if(wikiTextAreaUI == null) {
            wikiTextAreaUI = new WikiTextAreaUI();
        }
        return wikiTextAreaUI;
    }

    public boolean isModified() {
        return false;
    }

    public void apply() throws ConfigurationException {

    }

    public void reset() {

    }

    @Override
    public void disposeUIResources() {
        this.wikiTextAreaUI = null;
    }

}
