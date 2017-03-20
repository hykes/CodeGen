package me.hehaiyang.plugins.codegen.setting;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
public class FormatConfigurable implements SearchableConfigurable {

    private FormatForm formatForm;
    private FormatSetting formatSetting = FormatSetting.getInstance();

    public FormatConfigurable() {
    }

    @NotNull
    public String getId() {
        return "CodeGen";
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
        if(null == this.formatForm) {
            this.formatForm = new FormatForm();
        }
        return this.formatForm.mainPanel;
    }

    public boolean isModified() {
        return !this.formatSetting.getSqlFormat().equals(this.formatForm.mapperTextArea.getText())
                || !this.formatSetting.getMapperFormat().equals(this.formatForm.sqlTextArea.getText())
                || !this.formatSetting.getModelFormat().equals(this.formatForm.modelTextArea.getText()) ;
    }

    public void apply() throws ConfigurationException {
        this.formatSetting.setSqlFormat(this.formatForm.sqlTextArea.getText());
        this.formatSetting.setMapperFormat(this.formatForm.mapperTextArea.getText());
        this.formatSetting.setModelFormat(this.formatForm.modelTextArea.getText());
    }

    public void reset() {
        this.formatForm.mapperTextArea.setText(this.formatSetting.getMapperFormat());
        this.formatForm.sqlTextArea.setText(this.formatSetting.getSqlFormat());
        this.formatForm.modelTextArea.setText(this.formatSetting.getModelFormat());
    }

    @Override
    public void disposeUIResources() {

    }

}
