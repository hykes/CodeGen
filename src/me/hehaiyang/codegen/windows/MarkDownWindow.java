package me.hehaiyang.codegen.windows;

import com.google.common.base.Strings;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.setting.SettingManager;
import me.hehaiyang.codegen.utils.ParseUtils;
import me.hehaiyang.codegen.utils.PsiUtil;

import javax.swing.*;
import java.util.List;

public class MarkDownWindow extends JFrame {

    private final SettingManager settingManager = SettingManager.getInstance();
    private Project project;

    private JPanel codeGenJPanel;
    private JPanel paramsJPanel;
    private JTextField modelText;
    private JTextField tableText;
    private JLabel modelLabel;
    private JLabel tableLabel;
    private JPanel actionJpanel;
    private JButton cancel;
    private JButton sure;
    private JScrollPane codeJScrollPane;
    private JTextPane codeJTextPane;
    private JPanel tipsJPanel;
    private JLabel tipslabel;
    private JTextField modelNameText;
    private JTextField tableNameText;
    private JLabel modelNameLabel;
    private JLabel tableNameLabel;

    public MarkDownWindow(AnActionEvent anActionEvent) {
        setContentPane(codeGenJPanel);
        setTitle("CodeGen");

        this.project = PsiUtil.getProject(anActionEvent);

        codeJTextPane.requestFocus(true);

        this.init();

    }

    private void init() {

        sure.addActionListener(e -> {
            try {

                String model = modelText.getText().trim();
                String modelName = modelNameText.getText().trim();
                String table = tableText.getText().trim();
                String tableName = tableNameText.getText().trim();
                String markdown = codeJTextPane.getText().trim();

                if(Strings.isNullOrEmpty(model)){
                    this.setTipsVisbile(true, "Model不能为空！");
                    modelText.requestFocus(true);
                    return;
                }
                if(Strings.isNullOrEmpty(modelName)){
                    this.setTipsVisbile(true, "Model Name不能为空！");
                    modelNameText.requestFocus(true);
                    return;
                }
                if(Strings.isNullOrEmpty(table)){
                    this.setTipsVisbile(true, "Table不能为空！");
                    tableText.requestFocus(true);
                    return;
                }
                if(Strings.isNullOrEmpty(tableName)){
                    this.setTipsVisbile(true, "Table Name不能为空！");
                    tableNameText.requestFocus(true);
                    return;
                }
                if(Strings.isNullOrEmpty(markdown)){
                    this.setTipsVisbile(true, "表结构设计不能为空！");
                    codeJTextPane.requestFocus(true);
                    return;
                }

                List<Field> fields = ParseUtils.parseString(markdown);
                if (fields == null || fields.isEmpty()) {
                    setTipsVisbile(true, "表结构设计读取失败，请检查！");
                    codeJTextPane.requestFocus(true);
                    return;
                }


                this.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        cancel.addActionListener(e -> {
            dispose();
        });
    }

    /**
     * 设置tips
     * @param operator
     * @param tips
     */
    private void setTipsVisbile(boolean operator, String tips) {
        tipslabel.setText(tips);
        tipslabel.setVisible(operator);
    }

}



