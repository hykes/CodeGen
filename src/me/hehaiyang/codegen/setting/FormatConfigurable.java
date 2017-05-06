package me.hehaiyang.codegen.setting;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.ui.table.JBTable;
import com.yourkit.util.Strings;
import lombok.NoArgsConstructor;
import me.hehaiyang.codegen.model.CodeTemplate;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@NoArgsConstructor
public class FormatConfigurable implements SearchableConfigurable {

    private FormatSetting formatSetting = FormatSetting.getInstance();

    private FormatForm formatForm;

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
        if(formatForm == null) {
            formatForm = new FormatForm(formatSetting);
        }
        return formatForm.getMainPanel();
    }

    public boolean isModified() {

        // 模版更新
        TemplateEditPane template = formatForm.getEditPane();
        if(!formatSetting.getCodeTemplates().isEmpty()){
            CodeTemplate codeTemplate = formatSetting.getCodeTemplate(template.getCodeTemplate().getId());
            if (codeTemplate == null ||
                    !codeTemplate.getTemplate().equals(template.getCodeTemplate().getTemplate()) ||
                    !codeTemplate.getExtension().equals(template.getCodeTemplate().getExtension()) ||
                    !codeTemplate.getDisplay().equals(template.getCodeTemplate().getDisplay()) ||
                    !codeTemplate.getFilename().equals(template.getCodeTemplate().getFilename())) {
                return true;
            }
        }else if(!Strings.isNullOrEmpty(template.getCodeTemplate().getId())){
            return true;
        }

        // 参数更新
        if (formatSetting.getParams().size() != formatForm.getParamsTable().getRowCount()) {
            return true;
        }
        Map<String, String> params = formatSetting.getParams();
        DefaultTableModel tableModel = formatForm.getParamsTableModel();
        for(int i = 0; i< formatForm.getParamsTable().getRowCount(); i++){
            String key = tableModel.getValueAt(i, 0).toString();
            String value = tableModel.getValueAt(i, 1).toString();
            if(!params.containsKey(key)){
                return true;
            }else if(!params.get(key).equals(value)){
                return true;
            }
        }

        return false;
    }

    public void apply() throws ConfigurationException {

        // 保存模版
        formatSetting.addCodeTemplate(formatForm.getEditPane().getCodeTemplate());

        // 保存参数
        Map<String, String> params = new HashMap<>();
        DefaultTableModel tableModel = formatForm.getParamsTableModel();
        JBTable table = formatForm.getParamsTable();
        for(int i = 0;i< table.getRowCount(); i++){
            params.put(tableModel.getValueAt(i, 0).toString(), tableModel.getValueAt(i, 1).toString());
        }
        formatSetting.setParams(params);
        formatForm.refresh(formatSetting);
    }

    public void reset() {
        if (formatForm != null) {

            // 黑魔法，初始化配置
            if(formatForm.getParamsTable().getRowCount() == 1 &&
                    "init".equals(formatForm.getParamsTableModel().getValueAt(0, 0).toString())) {
                FormatSetting initSetting = new FormatSetting();
                formatSetting.setCodeTemplates(initSetting.getCodeTemplates());
                formatSetting.setParams(initSetting.getParams());
            }
            formatForm.refresh(formatSetting);
        }
    }

    @Override
    public void disposeUIResources() {
        this.formatForm = null;
    }

}
