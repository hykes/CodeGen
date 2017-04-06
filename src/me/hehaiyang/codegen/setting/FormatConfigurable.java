package me.hehaiyang.codegen.setting;

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
        if(null == formatForm) {
            formatForm = new FormatForm();
        }
        return formatForm.getMainPanel();
    }

    public boolean isModified() {
        return !this.formatSetting.getSqlFormat().equals(this.formatForm.mapperTextArea.getText())
                || !this.formatSetting.getMapperFormat().equals(this.formatForm.sqlTextArea.getText())
                || !this.formatSetting.getModelFormat().equals(this.formatForm.modelTextArea.getText())
                || !this.formatSetting.getReadFormat().equals(this.formatForm.readServiceTextArea.getText())
                || !this.formatSetting.getReadImplFormat().equals(this.formatForm.readServiceImplTextArea.getText())
                || !this.formatSetting.getWriteFormat().equals(this.formatForm.writeServiceTextArea.getText())
                || !this.formatSetting.getWriteImplFormat().equals(this.formatForm.writeServiceImplTextArea.getText())
                || !this.formatSetting.getControlFormat().equals(this.formatForm.controlTextArea.getText()) ;
    }

    public void apply() throws ConfigurationException {
        this.formatSetting.setSqlFormat(this.formatForm.sqlTextArea.getText());
        this.formatSetting.setMapperFormat(this.formatForm.mapperTextArea.getText());
        this.formatSetting.setModelFormat(this.formatForm.modelTextArea.getText());
        this.formatSetting.setControlFormat(this.formatForm.controlTextArea.getText());
        this.formatSetting.setReadFormat(this.formatForm.readServiceTextArea.getText());
        this.formatSetting.setReadImplFormat(this.formatForm.readServiceImplTextArea.getText());
        this.formatSetting.setWriteFormat(this.formatForm.writeServiceTextArea.getText());
        this.formatSetting.setWriteImplFormat(this.formatForm.writeServiceImplTextArea.getText());
    }

    public void reset() {
        this.formatForm.mapperTextArea.setText(this.formatSetting.getMapperFormat());
        this.formatForm.sqlTextArea.setText(this.formatSetting.getSqlFormat());
        this.formatForm.modelTextArea.setText(this.formatSetting.getModelFormat());
        this.formatForm.controlTextArea.setText(this.formatSetting.getControlFormat());
        this.formatForm.readServiceTextArea.setText(this.formatSetting.getReadFormat());
        this.formatForm.readServiceImplTextArea.setText(this.formatSetting.getReadImplFormat());
        this.formatForm.writeServiceTextArea.setText(this.formatSetting.getWriteFormat());
        this.formatForm.writeServiceImplTextArea.setText(this.formatSetting.getWriteImplFormat());
    }

    @Override
    public void disposeUIResources() {

    }

}
