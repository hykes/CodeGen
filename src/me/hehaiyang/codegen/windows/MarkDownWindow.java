package me.hehaiyang.codegen.windows;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.google.common.base.Strings;
import me.hehaiyang.codegen.constants.DefaultParams;
import me.hehaiyang.codegen.file.FileFactory;
import me.hehaiyang.codegen.file.FileProvider;
import me.hehaiyang.codegen.model.CodeGenContext;
import me.hehaiyang.codegen.model.CodeTemplate;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.setting.SettingManager;
import me.hehaiyang.codegen.setting.ui.TemplatesSetting;
import me.hehaiyang.codegen.utils.ParseUtils;
import me.hehaiyang.codegen.utils.PsiUtil;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkDownWindow extends JFrame {

    private final SettingManager settingManager = SettingManager.getInstance();
    private Project project;
    private FileFactory fileFactory;

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
        this.fileFactory = new FileFactory(PsiUtil.getProject(anActionEvent), PsiUtil.getIdeView(anActionEvent));

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
                Map<String, String> params = new HashMap<>();
                params.putAll(DefaultParams.getInstance());
                params.putAll(settingManager.getVariablesSetting().getParams());
                context.set$(params);
                WriteCommandAction.runWriteCommandAction(project, ()-> {
                    try {
                        for (CodeTemplate codeTemplate : settingManager.getTemplatesSetting().getGroups().get(0).getTemplates()) {
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
     * @param group 模版组
     * @param context 数据
     * @throws Exception
     */
    private void createFile(String group, CodeGenContext context) throws Exception{
        TemplatesSetting setting = settingManager.getTemplatesSetting();
        List<CodeTemplate> codeTemplates = setting.getTemplatesMap(setting.getGroups()).get(group);
        for(CodeTemplate codeTemplate: codeTemplates){
            FileProvider fileProvider = fileFactory.getInstance(codeTemplate.getExtension());
            fileProvider.create(codeTemplate.getTemplate(), context, codeTemplate.getFilename());
        }

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



