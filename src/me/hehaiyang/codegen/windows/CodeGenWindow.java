package me.hehaiyang.codegen.windows;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.base.Strings;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import me.hehaiyang.codegen.handlebars.HandlebarsFactory;
import me.hehaiyang.codegen.model.CodeGenContext;
import me.hehaiyang.codegen.model.Field;
import me.hehaiyang.codegen.model.SqlFileType;
import me.hehaiyang.codegen.setting.FormatSetting;
import me.hehaiyang.codegen.utils.BuilderUtil;
import me.hehaiyang.codegen.utils.ParseUtils;

import javax.swing.*;
import java.util.List;


public class CodeGenWindow extends JFrame {

    private final static Handlebars handlebars = HandlebarsFactory.getInstance();

    private FormatSetting formatSetting;


    AnActionEvent anActionEvent;

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
        this.anActionEvent=anActionEvent;
        textep.requestFocus(true);

        this.init();
    }

    private void createfile(AnActionEvent anActionEvent, Project project, String fileName, LanguageFileType fileType, String context) {
        PsiFile psiFile=  PsiFileFactory.getInstance(project).createFileFromText(fileName, fileType, context);
        PsiFile psiFilecurrent = anActionEvent.getData(LangDataKeys.PSI_FILE);
        psiFilecurrent.getContainingDirectory().add(psiFile);
    }

    private void init() {
        project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        psiClass = getPsiClassFromContext(anActionEvent);

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

                WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CodeGenContext context;
                            if(!onlyCheckBox.isSelected()) {
                                context = new CodeGenContext(modelName, tableName, fieldslist);

                                Template mapperTemplate = handlebars.compileInline(formatSetting.getMapperFormat());
                                String mapperContext = mapperTemplate.apply(BuilderUtil.transBean2Map(context));
                                createfile(anActionEvent, project, context.getModelName() + XmlFileType.DOT_DEFAULT_EXTENSION, XmlFileType.INSTANCE, mapperContext);

                                Template modelTemplate = handlebars.compileInline(formatSetting.getModelFormat());
                                String modelContext = modelTemplate.apply(BuilderUtil.transBean2Map(context));
                                createfile(anActionEvent, project, context.getModelName() + JavaFileType.DOT_DEFAULT_EXTENSION, JavaFileType.INSTANCE, modelContext);

                                Template sqlTemplate = handlebars.compileInline(formatSetting.getSqlFormat());
                                String sqlContext = sqlTemplate.apply(BuilderUtil.transBean2Map(context));
                                createfile(anActionEvent, project, context.getModelName() + SqlFileType.DOT_DEFAULT_EXTENSION, SqlFileType.INSTANCE, sqlContext);
                            }else {
                                context = new CodeGenContext(modelName);

                                Template contorlTemplate = handlebars.compileInline(formatSetting.getControlFormat());
                                String controlContext = contorlTemplate.apply(BuilderUtil.transBean2Map(context));
                                createfile(anActionEvent, project, context.getModelName()+"s" + JavaFileType.DOT_DEFAULT_EXTENSION, JavaFileType.INSTANCE, controlContext);

                                Template writeTemplate = handlebars.compileInline(formatSetting.getWriteFormat());
                                String wirteContext = writeTemplate.apply(BuilderUtil.transBean2Map(context));
                                createfile(anActionEvent, project, context.getModelName()+"WriteService" + JavaFileType.DOT_DEFAULT_EXTENSION, JavaFileType.INSTANCE, wirteContext);

                                Template writeImplTemplate = handlebars.compileInline(formatSetting.getWriteImplFormat());
                                String wirteImplContext = writeImplTemplate.apply(BuilderUtil.transBean2Map(context));
                                createfile(anActionEvent, project, context.getModelName()+"WriteServiceImpl" + JavaFileType.DOT_DEFAULT_EXTENSION, JavaFileType.INSTANCE, wirteImplContext);

                                Template readTemplate = handlebars.compileInline(formatSetting.getReadFormat());
                                String readContext = readTemplate.apply(BuilderUtil.transBean2Map(context));
                                createfile(anActionEvent, project, context.getModelName()+"ReadService" + JavaFileType.DOT_DEFAULT_EXTENSION, JavaFileType.INSTANCE, readContext);

                                Template readImplTemplate = handlebars.compileInline(formatSetting.getReadImplFormat());
                                String readImplContext = readImplTemplate.apply(BuilderUtil.transBean2Map(context));
                                createfile(anActionEvent, project, context.getModelName()+"ReadServiceImpl" + JavaFileType.DOT_DEFAULT_EXTENSION, JavaFileType.INSTANCE, readImplContext);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                CodeGenWindow.this.setVisibleWindow(false);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        cancel.addActionListener(e -> {
            dispose();
        });
    }

    public void setVisibleWindow(boolean operator) {
         dispose();
    }
    /**
     * 获取当前焦点下的类
     * @param e
     * @return
     */
    private PsiClass getPsiClassFromContext(AnActionEvent e) {

        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (psiFile == null || editor == null) {
            return null;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = psiFile.findElementAt(offset);

        return PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }

    public void setLableTextVisbile(boolean operator, String tips) {

        tipslabel.setText(tips);
        tipslabel.setVisible(operator);
    }
}


