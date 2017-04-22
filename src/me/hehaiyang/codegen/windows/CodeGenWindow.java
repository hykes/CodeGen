package me.hehaiyang.codegen.windows;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import me.hehaiyang.codegen.constants.DefaultParams;
import me.hehaiyang.codegen.file.FileFactory;
import me.hehaiyang.codegen.file.FileProvider;
import me.hehaiyang.codegen.model.CodeGenContext;
import me.hehaiyang.codegen.model.CodeTemplate;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.setting.FormatSetting;
import me.hehaiyang.codegen.utils.ParseUtils;
import me.hehaiyang.codegen.utils.PsiUtil;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class CodeGenWindow extends JFrame {

    private FormatSetting formatSetting;
    private Project project;
    private FileFactory fileFactory;

    private Map<String, CodeTemplate> templateMap;

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

    public CodeGenWindow(AnActionEvent anActionEvent) {
        setContentPane(codeGenJPanel);
        setTitle("CodeGen");

        this.project = PsiUtil.getProject(anActionEvent);
        this.formatSetting = FormatSetting.getInstance();
        this.fileFactory = new FileFactory(anActionEvent);
        this.templateMap = formatSetting.getCodeTemplates();

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

                // 组装数据
                CodeGenContext context = new CodeGenContext(model, modelName, table, tableName, fields);
                Map<String, String> params = Maps.newHashMap();
                params.putAll(DefaultParams.params);
                params.putAll(formatSetting.getParams());
                context.set$(params);
                WriteCommandAction.runWriteCommandAction(project, ()-> {
                    try {
                        for (CodeTemplate codeTemplate : templateMap.values()) {
                            FileProvider fileProvider = fileFactory.getInstance(codeTemplate.getExtension());
                            fileProvider.create(codeTemplate.getTemplate(), context, codeTemplate.getFilename());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

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
     * 模版+ 数据，生成文件
     * @param template 模版名称
     * @param context 数据
     * @throws Exception
     */
    private void createFile(String template, CodeGenContext context) throws Exception{
        CodeTemplate codeTemplate = formatSetting.getCodeTemplate(template);
        FileProvider fileProvider = fileFactory.getInstance(codeTemplate.getExtension());
        fileProvider.create(codeTemplate.getTemplate(), context, codeTemplate.getFilename());
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



