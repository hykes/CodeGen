package me.hehaiyang.codegen.setting;

import com.google.common.base.Strings;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.fileTypes.FileTypeManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.hehaiyang.codegen.model.CodeTemplate;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Desc: 模版配置
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TemplateEditPane extends JPanel {

    private JTextField id;
    private JTextField display;
    private JTextField extension;
    private JTextField filename;
    private Editor editor;

    public TemplateEditPane(){
        super();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        resetCodeTemplate(new CodeTemplate("", "", "", "", ""));
    }

    public TemplateEditPane(@NotNull String tempId){
        super();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        resetCodeTemplate(new CodeTemplate(tempId, "Unnamed", "", "", ""));
    }

    public TemplateEditPane(FormatSetting settings, String tempId) {
        super();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        CodeTemplate codeTemplate;
        if(Strings.isNullOrEmpty(tempId)){
            codeTemplate = new CodeTemplate();
        }else{
            codeTemplate = settings.getCodeTemplate(tempId);
            if (codeTemplate == null) {
                codeTemplate = new CodeTemplate(tempId, "Unnamed", "", "", "");
            }
        }

        resetCodeTemplate(codeTemplate);
    }

    private void resetCodeTemplate(CodeTemplate codeTemplate) {

        GridBagLayout layout = new GridBagLayout();
        JPanel jPanel = new JPanel(layout);

        id = new JTextField(codeTemplate.getId());
        display = new JTextField(codeTemplate.getDisplay());
        display.setColumns(15);

        extension = new JTextField(codeTemplate.getExtension());
        extension.setColumns(10);

        filename = new JTextField(codeTemplate.getFilename());
        filename.setColumns(15);

        GridBagConstraints s= new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;

        jPanel.add(new JLabel(" Name:"));
        jPanel.add(display, s);
        jPanel.add(new JLabel(" Filename:"));
        jPanel.add(filename, s);
        jPanel.add(new JLabel(" Extension:"));
        jPanel.add(extension, s);

        this.add(jPanel, BorderLayout.NORTH);

        editor = getEditor(codeTemplate.getTemplate(), codeTemplate.getExtension());
        this.add(editor.getComponent(), BorderLayout.CENTER);
    }

    /**
     * 创建编辑器
     * @param template
     * @param extension
     * @return
     */
    private Editor getEditor(String template, String extension) {
        template = Strings.isNullOrEmpty(template) ? "" : template;
        extension = Strings.isNullOrEmpty(extension) ? "vm" : extension;
        EditorFactory factory = EditorFactory.getInstance();
        Document velocityTemplate = factory.createDocument(template);
        Editor editor = factory.createEditor(velocityTemplate, null, FileTypeManager.getInstance()
                .getFileTypeByExtension(extension), false);
        EditorSettings editorSettings = editor.getSettings();
        editorSettings.setLineNumbersShown(true);
        return editor;
    }

    public CodeTemplate getCodeTemplate(){
        CodeTemplate codeTemplate = new CodeTemplate();
        codeTemplate.setId(id.getText());
        codeTemplate.setDisplay(display.getText());
        codeTemplate.setExtension(extension.getText());
        codeTemplate.setFilename(filename.getText());
        codeTemplate.setTemplate(editor.getDocument().getText());
        return codeTemplate;
    }

}
