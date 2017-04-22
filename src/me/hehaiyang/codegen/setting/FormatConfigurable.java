package me.hehaiyang.codegen.setting;

import com.google.common.collect.Maps;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;

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
            formatForm = new FormatForm(formatSetting);
        }
        return formatForm.getMainPanel();
    }

    public boolean isModified() {

//        // 模版更新
//        if (formatSetting.getCodeTemplates().size() != formatForm.getEditPaneMap().size()) {
//            return true;
//        }
//        for (Map.Entry<String, CodeTemplate> entry : formatForm.getTabTemplates().entrySet()) {
//            CodeTemplate codeTemplate = formatSetting.getCodeTemplate(entry.getKey());
//            if (codeTemplate == null ||
//                    !codeTemplate.getTemplate().equals(entry.getValue().getTemplate()) ||
//                    !codeTemplate.getType().equals(entry.getValue().getType()) ||
//                    !codeTemplate.getName().equals(entry.getValue().getName()) ||
//                    !codeTemplate.getFileName().equals(entry.getValue().getFileName())) {
//                return true;
//            }
//        }

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
//        for (Map.Entry<String, CodeTemplate> entry : formatForm.getTabTemplates().entrySet()) {
//            if (!entry.getValue().isValid()) {
//                throw new ConfigurationException(
//                        "Not property can be empty and classNumber should be a number");
//            }
//        }
//        formatSetting.setCodeTemplates(formatForm.getTabTemplates());

        // 保存参数
        Map<String, String> params = Maps.newHashMap();
        DefaultTableModel tableModel = formatForm.getParamsTableModel();
        JBTable table = formatForm.getParamsTable();
        for(int i = 0;i< table.getRowCount(); i++){
            params.put(tableModel.getValueAt(i, 0).toString(), tableModel.getValueAt(i, 1).toString());
        }
        formatSetting.setParams(params);
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
        }
    }

    @Override
    public void disposeUIResources() {
        this.formatForm = null;
    }

}
