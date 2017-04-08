package me.hehaiyang.codegen.windows;

import com.google.common.base.Strings;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import me.hehaiyang.codegen.file.FileFactory;
import me.hehaiyang.codegen.file.FileProvider;
import me.hehaiyang.codegen.model.CodeGenContext;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.setting.FormatSetting;
import me.hehaiyang.codegen.utils.ParseUtils;
import me.hehaiyang.codegen.utils.PsiUtil;

import javax.swing.*;
import java.util.List;


public class CodeGenWindow extends JFrame {

    private FormatSetting formatSetting;
    private AnActionEvent anActionEvent;

    private JPanel panel1;
    private JButton cancel;
    private JButton sure;
    private JLabel tipslabel;
    private JTextPane textep;
    private JTextField fieldTextField;
    private JTextField tableTextField;
    private JLabel fieldLabel;
    private JLabel tableLabel;
    private JCheckBox onlyCheckBox;
    private Project project;
    private PsiClass psiClass;

    public CodeGenWindow(AnActionEvent anActionEvent) {
        setContentPane(panel1);
        setTitle("CodeGen");
        this.anActionEvent = anActionEvent;
        this.project = PsiUtil.getProject(anActionEvent);
        this.psiClass = PsiUtil.getPsiClass(anActionEvent);
        textep.requestFocus(true);

        this.init();
    }

    private void init() {

        formatSetting = FormatSetting.getInstance();

        sure.addActionListener(e -> {
            try {

                String modelName = fieldTextField.getText().trim().toString();
                String tableName = tableTextField.getText().trim().toString();
                String markdown = textep.getText().trim().toString();

                if(Strings.isNullOrEmpty(modelName)){
                    CodeGenWindow.this.setLableTextVisbile(true, "Model名称不能为空");
                    return;
                }

                if(!onlyCheckBox.isSelected()){
                    if(Strings.isNullOrEmpty(tableName)){
                        CodeGenWindow.this.setLableTextVisbile(true, "Table名称不能为空");
                        return;
                    }
                    if(Strings.isNullOrEmpty(markdown)){
                        CodeGenWindow.this.setLableTextVisbile(true, "参数不能为空");
                        return;
                    }
                }

                List<Field> fieldslist = ParseUtils.parseString(textep.getText().trim().toString());
                if(!onlyCheckBox.isSelected()) {
                    if (fieldslist == null || fieldslist.size() == 0) {
                        CodeGenWindow.this.setLableTextVisbile(true, "参数格式不对，解析错误！");
                        return;
                    }
                }

                WriteCommandAction.runWriteCommandAction(project, ()-> {
                    try {
                        CodeGenContext context;
                        FileFactory fileFactory = new FileFactory(anActionEvent);
                        FileProvider xmlFileFactory = fileFactory.getInstance("xml");
                        FileProvider javaFileFactory = fileFactory.getInstance("java");
                        FileProvider sqlFileFactory = fileFactory.getInstance("sql");
                        if(!onlyCheckBox.isSelected()) {
                            context = new CodeGenContext(modelName, tableName, fieldslist);

                            xmlFileFactory.create(formatSetting.getCodeTemplate("mapper").getTemplate(), context, context.getModelName());

                            javaFileFactory.create(formatSetting.getCodeTemplate("model").getTemplate(), context, context.getModelName());

                            sqlFileFactory.create(formatSetting.getCodeTemplate("sql").getTemplate(), context, context.getModelName());

                        }else {
                            context = new CodeGenContext(modelName);

                            javaFileFactory.create(formatSetting.getCodeTemplate("controller").getTemplate(), context, context.getModelName()+"s");

                            javaFileFactory.create(formatSetting.getCodeTemplate("write").getTemplate(), context, context.getModelName()+"WriteService");

                            javaFileFactory.create(formatSetting.getCodeTemplate("writeImpl").getTemplate(), context, context.getModelName()+"WriteServiceImpl");

                            javaFileFactory.create(formatSetting.getCodeTemplate("read").getTemplate(), context, context.getModelName()+"ReadService");

                            javaFileFactory.create(formatSetting.getCodeTemplate("readImpl").getTemplate(), context, context.getModelName()+"ReadServiceImpl");
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                CodeGenWindow.this.setVisibleWindow();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        cancel.addActionListener(e -> {
            dispose();
        });
    }

    public void setVisibleWindow() {
        dispose();
    }

    public void setLableTextVisbile(boolean operator, String tips) {
        tipslabel.setText(tips);
        tipslabel.setVisible(operator);
    }

}



