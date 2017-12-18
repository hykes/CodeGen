package com.github.hykes.codegen.configurable.ui.template;

import com.github.hykes.codegen.model.CodeTemplate;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.util.ui.JBUI;
import com.github.hykes.codegen.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 模版编辑面板
 *
 * @author: hehaiyangwork@qq.com
 * @date: 2017/3/17
 */
public class TemplateEditor extends JPanel {

    private final JPanel textPanel = new JPanel(new GridLayout(3,4));
    private final JTextField id = new JTextField();
    private final JTextField display = new JTextField();
    private final JTextField extension = new JTextField();
    private final JTextField filename = new JTextField();
    private final JTextField subPath = new JTextField();
    private final JCheckBox isResources = new JCheckBox();
    private Editor editor;

    public TemplateEditor(){
        setLayout(new BorderLayout());
        setPreferredSize(JBUI.size(340, 100));
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        init();
    }

    private void init(){
        textPanel.setPreferredSize(new Dimension(0, 100));
        display.setSize(new Dimension(100, 20));
        extension.setSize(new Dimension(80,20));
        filename.setSize(new Dimension(100,20));
        subPath.setSize(new Dimension(100,20));

        textPanel.add(new JLabel("Name:"));
        textPanel.add(display);
        textPanel.add(new JLabel("Filename:"));
        textPanel.add(filename);
        textPanel.add(new JLabel("Extension:"));
        textPanel.add(extension);
        textPanel.add(new JLabel("SubPath:"));
        textPanel.add(subPath);
        textPanel.add(new JLabel("Resources:"));
        textPanel.add(isResources);

        this.add(textPanel, BorderLayout.NORTH);

        editor = emptyEditor();
        this.add(editor.getComponent(), BorderLayout.CENTER);
    }

    public void refresh(@NotNull CodeTemplate codeTemplate) {
        id.setText(codeTemplate.getId());
        display.setText(codeTemplate.getDisplay());
        extension.setText(codeTemplate.getExtension());
        filename.setText(codeTemplate.getFilename());
        subPath.setText(codeTemplate.getSubPath());
        isResources.setSelected(Objects.isNull(codeTemplate.getResources())?false: codeTemplate.getResources());
        editor = createEditor(codeTemplate.getTemplate(), codeTemplate.getExtension());

        this.removeAll();
        this.add(textPanel, BorderLayout.NORTH);
        this.add(editor.getComponent(), BorderLayout.CENTER);
    }

    private Editor emptyEditor(){
        return createEditor(null, null);
    }

    /**
     * 创建编辑器
     * @param template
     * @param extension
     * @return
     */
    private Editor createEditor(String template, String extension) {
        template = StringUtils.isEmpty(template) ? "" : template;
        extension = StringUtils.isEmpty(extension) ? "vm" : extension;
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
        codeTemplate.setId(id.getText().trim());
        codeTemplate.setDisplay(display.getText().trim());
        codeTemplate.setExtension(extension.getText().trim());
        codeTemplate.setFilename(filename.getText().trim());
        codeTemplate.setTemplate(editor.getDocument().getText());
        codeTemplate.setSubPath(subPath.getText().trim());
        codeTemplate.setResources(isResources.isSelected());
        return codeTemplate;
    }

}
