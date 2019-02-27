package com.github.hykes.codegen.configurable.ui.editor;

import com.github.hykes.codegen.model.CodeTemplate;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author hehaiyang@terminus.io
 * @date 2017/12/20
 */
public class TemplateEditorUI {
    private JPanel rootPanel;
    private JPanel infoPanel;
    private JTextField displayTextField;
    private JTextField filenameTextField;
    private JTextField extensionTextField;
    private JTextField subPathTextField;
    private JCheckBox resourceCheckBox;
    private JLabel displayLab;
    private JLabel filenameLab;
    private JLabel extensionLab;
    private JLabel subPathLab;
    private JLabel resourceLab;
    private JLabel orderLab;
    private JTextField orderTextField;

    private final JTextField id = new JTextField();

    private final EditorFactory factory = EditorFactory.getInstance();

    private Editor editor;

    public TemplateEditorUI() {
        $$$setupUI$$$();
        this.rootPanel.setPreferredSize(JBUI.size(340, 100));
        this.editor = emptyEditor();
        this.rootPanel.add(this.editor.getComponent(), BorderLayout.CENTER);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    }

    public void refresh(@NotNull CodeTemplate codeTemplate) {
        id.setText(codeTemplate.getId());
        displayTextField.setText(codeTemplate.getDisplay());
        extensionTextField.setText(codeTemplate.getExtension());
        filenameTextField.setText(codeTemplate.getFilename());
        subPathTextField.setText(codeTemplate.getSubPath());
        orderTextField.setText(StringUtils.nullOr(codeTemplate.getOrder(), 1) + "");
        resourceCheckBox.setSelected(Objects.isNull(codeTemplate.getResources()) ? false : codeTemplate.getResources());
        editor = createEditor(codeTemplate.getTemplate(), codeTemplate.getExtension());

        this.rootPanel.removeAll();
        this.rootPanel.add(infoPanel, BorderLayout.NORTH);
        this.rootPanel.add(editor.getComponent(), BorderLayout.CENTER);
    }

    private Editor emptyEditor() {
        return createEditor(null, null);
    }

    /**
     * 创建编辑器
     *
     * @param template
     * @param extension
     * @return
     */
    private Editor createEditor(String template, String extension) {
        template = StringUtils.isEmpty(template) ? "" : template;
        extension = StringUtils.isEmpty(extension) ? "vm" : extension;

        Document velocityTemplate = factory.createDocument(template);
        Editor editor = factory.createEditor(velocityTemplate, null, FileTypeManager.getInstance()
                .getFileTypeByExtension(extension), false);

        EditorSettings editorSettings = editor.getSettings();
        editorSettings.setLineNumbersShown(true);
        return editor;
    }

    public CodeTemplate getCodeTemplate() {
        CodeTemplate codeTemplate = new CodeTemplate();
        codeTemplate.setId(id.getText().trim());
        codeTemplate.setDisplay(displayTextField.getText().trim());
        codeTemplate.setExtension(extensionTextField.getText().trim());
        codeTemplate.setFilename(filenameTextField.getText().trim());
        codeTemplate.setTemplate(editor.getDocument().getText());
        codeTemplate.setSubPath(subPathTextField.getText().trim());
        codeTemplate.setResources(resourceCheckBox.isSelected());
        Integer order = StringUtils.isBlank(orderTextField.getText().trim()) ? Integer.valueOf(1) : Integer.valueOf(orderTextField.getText().trim());
        codeTemplate.setOrder(order);
        return codeTemplate;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setContentPane(new TemplateEditorUI().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = new JPanel();
        rootPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel.setLayout(new BorderLayout(0, 0));
        infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(infoPanel, BorderLayout.NORTH);
        displayLab = new JLabel();
        displayLab.setText("Name");
        infoPanel.add(displayLab, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        displayTextField = new JTextField();
        infoPanel.add(displayTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        filenameLab = new JLabel();
        filenameLab.setText("FileName");
        infoPanel.add(filenameLab, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        filenameTextField = new JTextField();
        infoPanel.add(filenameTextField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        extensionTextField = new JTextField();
        infoPanel.add(extensionTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        subPathLab = new JLabel();
        subPathLab.setText("SubPath");
        infoPanel.add(subPathLab, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        subPathTextField = new JTextField();
        infoPanel.add(subPathTextField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        resourceLab = new JLabel();
        resourceLab.setText("IsResource");
        infoPanel.add(resourceLab, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resourceCheckBox = new JCheckBox();
        resourceCheckBox.setText("");
        infoPanel.add(resourceCheckBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderLab = new JLabel();
        orderLab.setText("Order");
        infoPanel.add(orderLab, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        extensionLab = new JLabel();
        extensionLab.setText("Extension");
        infoPanel.add(extensionLab, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderTextField = new JTextField();
        infoPanel.add(orderTextField, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        orderLab.setLabelFor(orderTextField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}
