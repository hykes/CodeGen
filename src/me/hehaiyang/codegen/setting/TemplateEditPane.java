package me.hehaiyang.codegen.setting;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.fileTypes.FileTypeManager;
import lombok.Data;
import me.hehaiyang.codegen.model.CodeTemplate;

import javax.swing.*;
import java.awt.*;

/**
 * Desc: 模版配置
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/3/17
 */
@Data
public class TemplateEditPane {

    private JPanel templatePanel;
    private JTextField templateName;
    private JTextField templateType;
    private JTextField fileName;
    private JLabel templateNameLabel;
    private JLabel templateTypeLabel;
    private JLabel fileNameLabel;
    private JPanel configPanel;

    private Editor editor;

    public TemplateEditPane(FormatSetting settings, String template) {
        CodeTemplate codeTemplate = settings.getCodeTemplate(template);
        if (codeTemplate == null) {
            codeTemplate = new CodeTemplate();
        }

        templateName.setText(codeTemplate.getName());
        templateName.setFocusable(false);
        templateType.setText(codeTemplate.getType());
        templateType.setFocusable(false);
        fileName.setText(codeTemplate.getFileName());

        editor = getEditor(codeTemplate.getTemplate(), codeTemplate.getType());
        templatePanel.add(editor.getComponent(), BorderLayout.CENTER);

    }

    /**
     * 创建编辑器
     * @param template
     * @param extension
     * @return
     */
    private Editor getEditor(String template, String extension) {
        EditorFactory factory = EditorFactory.getInstance();
        Document velocityTemplate = factory.createDocument(template);
        Editor editor = factory.createEditor(velocityTemplate, null, FileTypeManager.getInstance()
                .getFileTypeByExtension(extension), false);

        EditorSettings editorSettings = editor.getSettings();
        editorSettings.setLineNumbersShown(true);
        return editor;
    }
}
